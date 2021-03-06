package com.example.ibrahim.resfor;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ibrahim.resfor.Client.ClientActivity;
import com.example.ibrahim.resfor.Restaurant.OrdersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    final String CHANNEL_ID="ORDER_RECIEVED";
    private NotificationManager notificationManager;
    private boolean newOrder=false;
    final int ID=1;
    private int howManyOrders=0;
    private boolean isClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        rootRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        final String userID = auth.getCurrentUser().getUid();
        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "Simple Notification",
                            NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        rootRef.child("Users").child(userID).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("Restaurant")){
                    isClient=false;
                    howManyOrders=0;
                    rootRef.child("Users").child(userID).child("orders").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            howManyOrders+=1;
                            notificate(ID,"New Order Recieved","You have "+howManyOrders+" orders!");
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                howManyOrders -= 1;
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    isClient=true;
                    rootRef.child("Users").child(userID).child("myOrders").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                            notificate(ID,"The delivery person arrived","come and take your order!");
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return super.onStartCommand(intent,flags,startId);
    }

    private void notificate(int id,String title,String Text){
        PendingIntent pendingIntent;
        if(!isClient) {
            Intent notificationIntent = new Intent(this, OrdersActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        }
        else{
            Intent notificationIntent = new Intent(this, ClientActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        }

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_orders)
                        .setContentTitle(title)
                        .setContentText(Text)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setOngoing(false)
                        .build();
        startForeground(1,notification);
        notificationManager.notify(id, notification);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
