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

public class menuAdapter extends ArrayAdapter<menuItem> {
    Context context;
    int resourse;
    List<menuItem> objects;
    public menuAdapter(Context context, int resource, List<menuItem> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resourse=resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resourse,null);
        ImageView image=view.findViewById(R.id.itemImage);
        TextView name=view.findViewById(R.id.foodName);
        TextView price =view.findViewById(R.id.foodPrice);
        TextView Des=view.findViewById(R.id.foodDescription);
        final menuItem row=objects.get(position);

        name.setText(row.getName());
        price.setText(row.getPrice());
        Des.setText(row.getDescription());

       /* text.setText(row.getText());

        if(row.getImg()==0){
            image.setImageResource(R.drawable.ic_orders);
        }else if(row.getImg()==1){
            image.setImageResource(R.drawable.ic_menu);

        }*/

        return view;
    }

}
