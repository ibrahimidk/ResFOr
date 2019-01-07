package com.example.ibrahim.resfor.Client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ibrahim.resfor.R;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantList> {
    Context context;
    int resource;
    List<RestaurantList> restaurantLists;
    public RestaurantAdapter(Context context, int resource,List<RestaurantList> restaurantLists) {
        super(context, resource, restaurantLists);
        this.context=context;
        this.resource=resource;
        this.restaurantLists=restaurantLists;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, null);

        TextView name=view.findViewById(R.id.RestaurantName);
        TextView phone =view.findViewById(R.id.RestaurantPhone);
        Button order_btn=view.findViewById(R.id.order_btn);
        Button menu_btn=view.findViewById(R.id.menu_btn);

        RestaurantList restaurant= restaurantLists.get(position);

        name.setText(restaurant.getName());
        phone.setText(restaurant.getPhone());


        return view;
    }
}
