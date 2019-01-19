package com.example.ibrahim.resfor.Restaurant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ibrahim.resfor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private ListView menuList;
    List<menuItem> menuItemList;
    List<String> keys;
    private Button btnBack,btnAdd;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
   private menuAdapter adapter;

    Dialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        menuList=findViewById(R.id.menuList);
        btnBack=findViewById(R.id.backBtn);
        btnAdd=findViewById(R.id.addBtn);
        final String userID = auth.getCurrentUser().getUid();
        menuItemList=new ArrayList<>();
        keys=new ArrayList<>();

        rootRef.child("Users").child(userID).child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuItemList.clear();
                keys.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    menuItem item=data.getValue(menuItem.class);
                    keys.add(data.getKey());
                    menuItemList.add(item);
                }
                adapter=new menuAdapter(MenuActivity.this,R.layout.menu_list_row,menuItemList);
                menuList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        menuList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                AlertDialog diaBox = AskOption(menuItemList.get(index));
                diaBox.show();
                return true;
            }
        });

       menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               AlertDialog diaBox = ShowDetails(menuItemList.get(position),position);
               diaBox.show();
           }
       });


/**********************************************************/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            finish();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,AddItemActivity.class));
            }
        });

    }



    /**********************************************************************************************************/

    private AlertDialog ShowDetails(final menuItem i, final int position)
    {
        LayoutInflater inflater=LayoutInflater.from(this);
        final View view =inflater.inflate(R.layout.activity_add_item,null);
        final EditText name=view.findViewById(R.id.itemAddName);
        final EditText price=view.findViewById(R.id.itemAddPrice);
        final EditText description=view.findViewById(R.id.itemAddDes);
        Button button=view.findViewById(R.id.AddItemBtn);
        button.setVisibility(View.GONE);
        final ImageView image=view.findViewById(R.id.itemAddImage);
        name.setText(i.getName());
        price.setText(i.getPrice());
        description.setText(i.getDescription());
        if( i.getImage()!=null && !TextUtils.equals(i.getImage(),"") && !TextUtils.equals(i.getImage()," ")){
            Picasso.with(this).load(i.getImage()).into(image);
        }

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Item Details")


                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        final String userID = auth.getCurrentUser().getUid();
                        HashMap items =new HashMap();
                        items.put("name",name.getText().toString());
                        items.put("price",price.getText().toString());
                        items.put("description",description.getText().toString());
                        items.put("image",i.getImage());
                        rootRef.child("Users").child(userID).child("menu").child(keys.get(position)).updateChildren(items);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setView(view)
                .create();
        return myQuittingDialogBox;

    }




    /***********************************************************************************************************/


    private AlertDialog AskOption(final menuItem i)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")


                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        auth = FirebaseAuth.getInstance();
                        rootRef= FirebaseDatabase.getInstance().getReference();
                        final String userID = auth.getCurrentUser().getUid();
                        rootRef.child("Users").child(userID).child("menu").orderByChild("name").equalTo(i.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                                    snapshot.getRef().removeValue();
                                    Toast.makeText(MenuActivity.this, i.getName()+" removed successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }


}
