package com.example.ibrahim.resfor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ibrahim.resfor.AccountActivity.LoginActivity;
import com.example.ibrahim.resfor.Client.ClientActivity;
import com.example.ibrahim.resfor.Restaurant.RestaurantActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        if (auth.getCurrentUser() != null) {
            final String userID = auth.getCurrentUser().getUid();
            rootRef.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(TextUtils.equals(dataSnapshot.child("type").getValue().toString(),"Restaurant")){
                        Log.d(">>>>>>>", "onDataChange: ");
                        startActivity(new Intent(WelcomeActivity.this, RestaurantActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(WelcomeActivity.this, ClientActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //startActivity(new Intent(LoginActivity.this, ClientActivity.class));
            //finish();
        }else{
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }

        // Start long running operation in a background thread
    }
}
