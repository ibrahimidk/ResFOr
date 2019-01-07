package com.example.ibrahim.resfor.Restaurant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibrahim.resfor.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<mainList> {
    Context context;
    int resourse;
    List<mainList> objects;

    public CustomAdapter( Context context, int resource,  List<mainList> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resourse=resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resourse,null);
        ImageView image=view.findViewById(R.id.image);
        TextView text=view.findViewById(R.id.text);
        final mainList row=objects.get(position);

        text.setText(row.getText());

        if(row.getImg()==0){
            image.setImageResource(R.drawable.ic_orders);
        }else if(row.getImg()==1){
            image.setImageResource(R.drawable.ic_menu);

        }

        return view;
    }
}
