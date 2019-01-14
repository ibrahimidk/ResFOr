package com.example.ibrahim.resfor.Restaurant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibrahim.resfor.AccountActivity.LoginActivity;
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

public class RestaurantActivity extends AppCompatActivity  {
private ListView listView;

private mainList[] objects;



List<mainList> list;

    private FirebaseAuth auth;
    private DatabaseReference rootRef,itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

       // Log.d(">>>", "onCreate: ");

        rootRef= FirebaseDatabase.getInstance().getReference();
        itemRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        

        listView=findViewById(R.id.myListView);

        objects=new mainList[2];
       objects[0]=new mainList(0,"Menu");
        objects[1]=new mainList(1,"Orders");
        list=new ArrayList<>();
        list.add(objects[0]);
        list.add(objects[1]);
        final CustomAdapter adapter=new CustomAdapter(this,R.layout.mainlistrow,list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
            //menu
                    startActivity(new Intent(RestaurantActivity.this,MenuActivity.class));


                }else if(position==1){
            //orders
                    startActivity(new Intent(RestaurantActivity.this,OrdersActivity.class));

                }
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
