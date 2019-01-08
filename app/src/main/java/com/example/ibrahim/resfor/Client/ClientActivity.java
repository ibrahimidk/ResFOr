package com.example.ibrahim.resfor.Client;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import com.example.ibrahim.resfor.R;
import com.example.ibrahim.resfor.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {


    List<RestaurantList> restaurantLists;
    ListView listView;
    RestaurantAdapter restaurantAdapter;
    private FirebaseAuth auth;
    private DatabaseReference rootRef ,typeRef;
    private DataSnapshot data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        listView=findViewById(R.id.RestaurantList);

        auth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        typeRef= FirebaseDatabase.getInstance().getReference();
        rootRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<RestaurantList> users = new ArrayList<>();
                for(DataSnapshot user:dataSnapshot.getChildren()){
                    RestaurantList us = user.getValue(RestaurantList.class);
                    if(TextUtils.equals(us.getType(),"Restaurant")){
                        users.add(us);
                    }
                }
                restaurantAdapter=new RestaurantAdapter(ClientActivity.this,R.layout.restaurant_in_the_client,users);
                listView.setAdapter(restaurantAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
