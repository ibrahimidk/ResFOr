package com.example.ibrahim.resfor.Client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibrahim.resfor.AccountActivity.LoginActivity;
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
    ListView RestaurantListView;
    RestaurantAdapter restaurantAdapter;




    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        RestaurantListView=findViewById(R.id.RestaurantList);

        auth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<RestaurantList> users = new ArrayList<>();
                for(DataSnapshot user:dataSnapshot.getChildren()){
                    RestaurantList us = user.getValue(RestaurantList.class);
                    if(TextUtils.equals(us.getType(),"Restaurant")){
                        users.add(us);

                    }
                }
                restaurantAdapter=new RestaurantAdapter(ClientActivity.this,R.layout.restaurant_in_the_client,users);
                RestaurantListView.setAdapter(restaurantAdapter);

                RestaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(ClientActivity.this, users.get(i).getEmail(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //item is used to access the position of an option
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.sign_out_btn){
            auth.signOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        return  true;
    }

}
