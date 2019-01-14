package com.example.ibrahim.resfor.Restaurant;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibrahim.resfor.R;
import com.squareup.picasso.Picasso;

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
       // https://firebasestorage.googleapis.com/v0/b/resfor-bb2cb.appspot.com/o/VfFhIIerUAeCWyg2MUSieGtcKKH2%2F-LW70e5UULLVzEV5VIjV.jpg?alt=media&token=a1a2f71c-a7ba-49b1-8b3d-2908550e0c94

        name.setText(row.getName());
        price.setText(row.getPrice());
        Des.setText(row.getDescription());
        if( row.getImage()!=null && !TextUtils.equals(row.getImage(),"") && !TextUtils.equals(row.getImage()," ")){
            Picasso.with(context).load(row.getImage()).into(image);
            Log.d(">>>>", "getView:"+row.getImage() + "ss");
        }

       /* text.setText(row.getText());

        if(row.getImg()==0){
            image.setImageResource(R.drawable.ic_orders);
        }else if(row.getImg()==1){
            image.setImageResource(R.drawable.ic_menu);

        }*/

        return view;
    }

}
