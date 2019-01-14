package com.example.ibrahim.resfor.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibrahim.resfor.R;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<String> {
    Context context;
    int resourse;
    List<String> objects;



    public OrderAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resourse=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resourse,null);
        TextView text=view.findViewById(R.id.orderName);
        text.setText("Order "+ position);
        return view;
    }
}
