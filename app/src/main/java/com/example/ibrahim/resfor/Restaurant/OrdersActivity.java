package com.example.ibrahim.resfor.Restaurant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ibrahim.resfor.MyService;
import com.example.ibrahim.resfor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private ListView ordersList;
    private ListView orderMenuListView;
    private TextView nameTxt,phoneTxt,locationTxt;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    List<order> orders;
    List<String> ordersNamesList;
    List<menuItem> orderMenuList;
    private OrderAdapter orderAdapter;
    private menuAdapter orderMenuAdapter;
    private boolean itemClicked=false;
    private Button back,recieve;
    final String CHANNEL_ID="ORDER_RECIEVED";
    private NotificationManager notificationManager;
    private boolean newOrder=false;
    final int ID=1;
    private int howManyOrders=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        rootRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        ordersList=findViewById(R.id.ordersList);
        orderMenuListView=findViewById(R.id.orderMenuList);
        nameTxt=findViewById(R.id.orderName);
        locationTxt=findViewById(R.id.orderLocation);
        phoneTxt=findViewById(R.id.orderPhone);
        back=findViewById(R.id.backBtnOrders);
        recieve=findViewById(R.id.ReceiveBtn);
        orders=new ArrayList<>();
        orderMenuList=new ArrayList<>();
        ordersNamesList=new ArrayList<>();
        final String userID = auth.getCurrentUser().getUid();



        notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "Simple Notification",
                            NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        rootRef.child("Users").child(userID).child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orders.clear();
                ordersNamesList.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    final order item=data.getValue(order.class);

                    rootRef.child("Users").child(userID).child("orders").child(data.getKey()).child("theOrder").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            List<menuItem> menuItemList=new ArrayList<>();
                            howManyOrders=0;
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                menuItem orderItems=data.getValue(menuItem.class);
                                menuItemList.add(orderItems);
                                howManyOrders++;
                            }
                            item.setOrderList(menuItemList);
                         //   Log.d(">>>", "onDataChange: "+item.getOrderList().get(0).getName());
                            orders.add(item);
                            ordersNamesList.add(item.getName());
                            orderAdapter=new OrderAdapter(OrdersActivity.this,R.layout.order_row,ordersNamesList);
                            ordersList.setAdapter(orderAdapter);
                            if(newOrder) {
                                if (howManyOrders > 0) {
                                    newOrder = false;
                                    notificate(ID, "New Order Recieved", "You have " + howManyOrders + " orders!");
                                }
                            }else{
                                newOrder = true;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //Log.d(">>>", "onDataChange: "+item.getOrderList());
                    // Log.d(">>>", "onDataChange: " +ata.getKey()d);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


     ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // startService(new Intent(OrdersActivity.this, MyService.class));
             if(!itemClicked){
                 back.setVisibility(View.VISIBLE);
                 recieve.setVisibility(View.VISIBLE);
                 orderMenuList = orders.get(position).getOrderList();
                 orderMenuAdapter = new menuAdapter(OrdersActivity.this, R.layout.menu_list_row, orderMenuList);
                 phoneTxt.setText(orders.get(position).getNumber());
                 phoneTxt.setVisibility(View.VISIBLE);
                 locationTxt.setText(orders.get(position).getLocation());
                 locationTxt.setVisibility(View.VISIBLE);
                 nameTxt.setText(orders.get(position).getName());
                 nameTxt.setVisibility(View.VISIBLE);
                 orderMenuListView.setAdapter(orderMenuAdapter);
                 orderMenuListView.setVisibility(View.VISIBLE);
                 ordersList.setVisibility(View.GONE);
                 itemClicked=true;
             }

         }
     });


     back.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             itemClicked=false;
             back.setVisibility(View.GONE);
             recieve.setVisibility(View.GONE);
             phoneTxt.setVisibility(View.GONE);
             locationTxt.setVisibility(View.GONE);
             nameTxt.setVisibility(View.GONE);
             orderMenuListView.setVisibility(View.GONE);
             ordersList.setVisibility(View.VISIBLE);
         }
     });

    }

    private void notificate(int id,String title,String Text){
        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_orders)
                        .setContentTitle(title)
                        .setContentText(Text)
                        .build();

        notificationManager.notify(id, notification);

    }




}
