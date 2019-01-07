package com.example.ibrahim.resfor.Restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ibrahim.resfor.R;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
private ListView listView;
private mainList[] objects;
List<mainList> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        listView=findViewById(R.id.myListView);
        objects=new mainList[2];
       objects[0]=new mainList(0,"Menu");
        objects[1]=new mainList(1,"Orders");
        list=new ArrayList<>();
        list.add(objects[0]);
        list.add(objects[1]);
        final CustomAdapter adapter=new CustomAdapter(this,R.layout.mainlistrow,list);
        listView.setAdapter(adapter);
    }
}
