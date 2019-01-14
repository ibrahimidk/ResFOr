package com.example.ibrahim.resfor.Client;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ibrahim.resfor.AccountActivity.LoginActivity;
import com.example.ibrahim.resfor.R;
import com.example.ibrahim.resfor.Restaurant.MenuActivity;
import com.example.ibrahim.resfor.Restaurant.menuAdapter;
import com.example.ibrahim.resfor.Restaurant.menuItem;
import com.example.ibrahim.resfor.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientActivity extends AppCompatActivity {


    ListView ListView1, cartList;
    TextView ordertxt;
    String theClientLocation="";
    Button back_btn, cart_btn, send_order_btn;
    RestaurantAdapter restaurantAdapter;
    menuAdapter menuadapter;
    boolean inmenu = false, in_the_cart_list = false;

    private LocationManager locationManager;


    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ListView1 = findViewById(R.id.RestaurantList);

        cartList = findViewById(R.id.cart);
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        back_btn = findViewById(R.id.BackBtn);
        cart_btn = findViewById(R.id.cartBtn);
        ordertxt = findViewById(R.id.ordertxt);
        send_order_btn = findViewById(R.id.send_order_btn);

        rootRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                final List<RestaurantList> users = new ArrayList<>();
                final List<String> id = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RestaurantList user = data.getValue(RestaurantList.class);
                    if (TextUtils.equals(user.getType(), "Restaurant")) {
                        users.add(user);
                        id.add(data.getKey());
                    }
                }
                if (inmenu) {
                    back_btn.setVisibility(View.VISIBLE);
                    cart_btn.setVisibility(View.VISIBLE);
                    cartList.setVisibility(View.VISIBLE);
                    ordertxt.setVisibility(View.VISIBLE);
                }
                restaurantAdapter = new RestaurantAdapter(ClientActivity.this, R.layout.restaurant_in_the_client, users);
                ListView1.setAdapter(restaurantAdapter);
                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!in_the_cart_list) {
                            restaurantAdapter = new RestaurantAdapter(ClientActivity.this, R.layout.restaurant_in_the_client, users);
                            ListView1.setAdapter(restaurantAdapter);
                            back_btn.setVisibility(View.GONE);
                            cart_btn.setVisibility(View.GONE);
                            cartList.setVisibility(View.GONE);
                            ordertxt.setVisibility(View.GONE);
                            send_order_btn.setVisibility(View.GONE);
                            inmenu = false;

                        } else {
                            ListView1.setVisibility(View.VISIBLE);
                            in_the_cart_list = false;
                        }
                    }
                });
                final List<menuItem> carts = new ArrayList<>();

                cart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cartList.getCount() != 0) {
                            in_the_cart_list = true;
                            ListView1.setVisibility(View.GONE);
                            send_order_btn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ClientActivity.this, "choose your order first fucker", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                send_order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(theClientLocation)) {
                            Location location;
                            locationManager = (LocationManager) ClientActivity.this.getSystemService(LOCATION_SERVICE);
                            if (ActivityCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ClientActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                return;
                            } else {
                                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                                LocationManager locationManager = (LocationManager) getSystemService(ClientActivity.LOCATION_SERVICE);
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }

                            Geocoder geocoder = new Geocoder(ClientActivity.this, Locale.getDefault());
                            List<Address> addresses = new ArrayList<Address>();
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (Exception ioException) {
                                Log.e(">>>>", "Error in getting address for the location");
                            }

                            if (addresses.size() > 0) {
                                theClientLocation += addresses.get(0).getThoroughfare() + " " + addresses.get(0).getFeatureName() + " " + addresses.get(0).getLocality();
                            }

                            //if the address is empty.
                            else{
                                AlertDialog diaBox = AskOption();
                                diaBox.show();
                            }
                        }

                        Log.d(">>>>1", "onClick: " + theClientLocation);

                    }
                });




                ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                        if(!inmenu) {
                            carts.clear();
                            rootRef.child("Users").child(id.get(i)).child("menu").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final List<menuItem> menuList = new ArrayList<>();
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        menuItem mi = data.getValue(menuItem.class);
                                        menuList.add(mi);
                                    }
                                    menuadapter = new menuAdapter(ClientActivity.this, R.layout.menu_list_row, menuList);
                                    ListView1.setAdapter(menuadapter);
                                    inmenu = true;
                                    back_btn.setVisibility(View.VISIBLE);
                                    cart_btn.setVisibility(View.VISIBLE);
                                    cartList.setVisibility(View.VISIBLE);
                                    ordertxt.setVisibility(View.VISIBLE);
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{
                            menuItem mi = ((menuItem) adapterView.getItemAtPosition(i));
                            carts.add(mi);
                        }
                        menuadapter = new menuAdapter(ClientActivity.this, R.layout.menu_list_row, carts);
                        cartList.setAdapter(menuadapter);
                        cartList.setSelection(menuadapter.getCount()-1);
                    }
                });
                cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!in_the_cart_list) {
                            carts.remove(i);
                            menuadapter.notifyDataSetChanged();
                        }
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    /******************/
    private AlertDialog AskOption()
    {
        final EditText input = new EditText(ClientActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Details")
                .setMessage("insert your location")


                .setPositiveButton("Add Location", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        if(!TextUtils.isEmpty(input.getText().toString())){
                            theClientLocation=input.getText().toString();
                        }
                        else{
                            Toast.makeText(ClientActivity.this, "Enter the location or turn your location on fucker", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setView(input)
                .create();
        return myQuittingDialogBox;

    }



}
