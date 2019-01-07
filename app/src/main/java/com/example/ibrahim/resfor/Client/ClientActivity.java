package com.example.ibrahim.resfor.Client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ibrahim.resfor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClientActivity extends AppCompatActivity {


    List<RestaurantList> restaurantLists;
    ListView listView;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private DataSnapshot data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        auth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        if (auth.getCurrentUser() != null) {
            final String userID = auth.getCurrentUser().getUid();


        }

    }
}
