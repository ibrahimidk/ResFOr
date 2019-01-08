package com.example.ibrahim.resfor.Restaurant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ibrahim.resfor.R;

public class AddItemDialog extends AppCompatDialogFragment {
private ImageView image;
private EditText name,price,description;
private myDialogListener listener;
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.add_item,null);

        builder.setView(view).setTitle("Add Item")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Image=" ";
                String Name=name.getText().toString();
                String Price=price.getText().toString();
                String Des=description.getText().toString();
                listener.applyAdd(Image,Name,Price,Des);
            }
        });
        image=view.findViewById(R.id.itemAddImage);
        name=view.findViewById(R.id.itemAddName);
        price=view.findViewById(R.id.itemAddPrice);
        description=view.findViewById(R.id.itemAddDes);
        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
         listener=(myDialogListener) context;
        }catch (ClassCastException e){
           throw new ClassCastException(context.toString() + "must implement myDialogListener");
        }
    }

    public interface myDialogListener{
        void applyAdd(String image,String name,String price,String description);
    }
}
