package com.example.ibrahim.resfor.Restaurant;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibrahim.resfor.AccountActivity.SignupActivity;
import com.example.ibrahim.resfor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements AddItemDialog.myDialogListener {
private ListView listView;
private ListView menuList;
private mainList[] objects;
private boolean pressed=false;
List<menuItem> menuItemList;
private Button btnBack,btnAdd;
List<mainList> list;

    private FirebaseAuth auth;
    private DatabaseReference rootRef,itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Log.d(">>>", "onCreate: ");

        rootRef= FirebaseDatabase.getInstance().getReference();
        itemRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        
        menuList=findViewById(R.id.menuList);
        listView=findViewById(R.id.myListView);
        btnBack=findViewById(R.id.backBtn);
        btnAdd=findViewById(R.id.addBtn);
        objects=new mainList[2];
       objects[0]=new mainList(0,"Menu");
        objects[1]=new mainList(1,"Orders");
        list=new ArrayList<>();
        list.add(objects[0]);
        list.add(objects[1]);
        final CustomAdapter adapter=new CustomAdapter(this,R.layout.mainlistrow,list);
        listView.setAdapter(adapter);

        if(pressed){
            menuList.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
        }else if(!pressed){
            menuList.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    menuList.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                }else if(position==1){
                    btnBack.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuList.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                pressed=true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog addDialog =new AddItemDialog();
                addDialog.show(getSupportFragmentManager(),"Add Dialog");
            }
        });


    }

    public void applyAdd(final String image, final String name, final String price, final String description){
        final String userID = auth.getCurrentUser().getUid();
        DatabaseReference itemKeyRef=rootRef.child("Users").child(userID).child("menu").push();
        String itemKey = itemKeyRef.getKey();
        itemRef = rootRef.child("Users").child(userID).child("menu");
        HashMap items =new HashMap();
        items.put("name",name);
        items.put("price",price);
        items.put("description",description);
        items.put("image",image);
        rootRef.child("Users").child(userID).child("menu").child(itemKey).updateChildren(items).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(RestaurantActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RestaurantActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
