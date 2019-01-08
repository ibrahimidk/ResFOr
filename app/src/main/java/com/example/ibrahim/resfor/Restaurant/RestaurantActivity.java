package com.example.ibrahim.resfor.Restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ibrahim.resfor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
private ListView listView;
private ListView menuList;
private mainList[] objects;
List<menuItem> menuItemList;
private Button btnBack,btnAdd;
List<mainList> list;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        
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
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
