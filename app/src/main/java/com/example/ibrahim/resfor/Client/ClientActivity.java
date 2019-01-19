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
import android.net.Uri;
import android.provider.Settings;
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
import com.example.ibrahim.resfor.MyService;
import com.example.ibrahim.resfor.R;
import com.example.ibrahim.resfor.Restaurant.MenuActivity;
import com.example.ibrahim.resfor.Restaurant.menuAdapter;
import com.example.ibrahim.resfor.Restaurant.menuItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.support.v4.app.ServiceCompat.stopForeground;

public class ClientActivity extends AppCompatActivity implements LocationListener{


    int idIndex = 0;
    ListView ListView1, cartList;
    TextView ordertxt;
    String theClientLocation = "";
    Button back_btn, cart_btn, send_order_btn, searchbtn;
    RestaurantAdapter restaurantAdapter;
    menuAdapter menuadapter;
    boolean inmenu = false, in_the_cart_list = false;
    EditText searchtxt;



    private LocationManager locationManager;


    private FirebaseAuth auth;
    private DatabaseReference rootRef, orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        ListView1 = findViewById(R.id.RestaurantList);

        cartList = findViewById(R.id.cart);
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        orderRef = FirebaseDatabase.getInstance().getReference();
        back_btn = findViewById(R.id.BackBtn);
        cart_btn = findViewById(R.id.cartBtn);
        ordertxt = findViewById(R.id.ordertxt);
        send_order_btn = findViewById(R.id.send_order_btn);
        searchbtn=findViewById(R.id.searchbtn);
        searchtxt=findViewById(R.id.searchtxt);



        rootRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final List<RestaurantList> users = new ArrayList<>();
                final List<String> id = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RestaurantList user = data.getValue(RestaurantList.class);
                    if (TextUtils.equals(user.getType(), "Restaurant") ) {
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
                searchbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        users.clear();
                        id.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            RestaurantList user = data.getValue(RestaurantList.class);
                            if (TextUtils.equals(user.getType(), "Restaurant") ) {
                                users.add(user);
                                id.add(data.getKey());
                            }
                        }
                        if(!TextUtils.isEmpty(searchtxt.getText().toString())) {
                            for (int i = 0; i < users.size(); i++) {
                                if (!TextUtils.equals(users.get(i).getRestaurant_location(), searchtxt.getText().toString())) {
                                    users.remove(i);
                                    id.remove(i);
                                    i--;
                                }
                            }
                        }
                        restaurantAdapter = new RestaurantAdapter(ClientActivity.this, R.layout.restaurant_in_the_client, users);
                        ListView1.setAdapter(restaurantAdapter);
                    }
                });
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
                            searchtxt.setVisibility(View.VISIBLE);
                            searchbtn.setVisibility(View.VISIBLE);
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
                            if (ActivityCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(ClientActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                            else if(isPermissionToReadGPSLocationOK()) {
                                trackLocation(view);
                                in_the_cart_list = true;
                                ListView1.setVisibility(View.GONE);
                                send_order_btn.setVisibility(View.VISIBLE);
                            }
                            else{
                                AlertDialog diaBox = AskOption();
                                diaBox.show();
                            }
                        } else {
                            Toast.makeText(ClientActivity.this, "choose your order first", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                send_order_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startService(new Intent(ClientActivity.this, MyService.class));

                        //push the order to the database
                        DatabaseReference itemKeyRef = rootRef.child("Users").child(id.get(idIndex)).child("orders").push();
                        final String itemKey = itemKeyRef.getKey();
                        orderRef = rootRef.child("Users").child(id.get(idIndex)).child("orders").child(itemKey);
                        final HashMap order = new HashMap();
                        final HashMap myOrder=new HashMap();
                        rootRef.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                order.put("clientID",auth.getCurrentUser().getUid());
                                order.put("name", dataSnapshot.child("name").getValue().toString());
                                order.put("number", dataSnapshot.child("phone").getValue().toString());
                                order.put("location", theClientLocation);
                                order.put("theOrder", carts);
                                orderRef.updateChildren(order);
                                myOrder.put("orderID",itemKey);
                                rootRef.child("Users").child(auth.getCurrentUser().getUid()).child("myOrders").child(itemKey).setValue("");
                                carts.clear();
                                searchtxt.setText("");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        startService(new Intent(ClientActivity.this, MyService.class));
                        ListView1.setVisibility(View.VISIBLE);
                        searchtxt.setVisibility(View.VISIBLE);
                        searchbtn.setVisibility(View.VISIBLE);
                        back_btn.setVisibility(View.GONE);
                        cart_btn.setVisibility(View.GONE);
                        cartList.setVisibility(View.GONE);
                        ordertxt.setVisibility(View.GONE);
                        send_order_btn.setVisibility(View.GONE);
                        inmenu = false;
                        in_the_cart_list = false;


                    }
                });


                ListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        if (!inmenu) {
                            searchtxt.setVisibility(View.GONE);
                            searchbtn.setVisibility(View.GONE);
                            idIndex = i;
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
                        } else {
                            menuItem mi = ((menuItem) adapterView.getItemAtPosition(i));
                            carts.add(mi);
                        }
                        menuadapter = new menuAdapter(ClientActivity.this, R.layout.menu_list_row, carts);
                        cartList.setAdapter(menuadapter);
                        cartList.setSelection(menuadapter.getCount() - 1);
                    }
                });
                cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!in_the_cart_list) {
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
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //item is used to access the position of an option
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.sign_out_btn) {
            stopService(new Intent(this, MyService.class));
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return true;
    }


    /******************/
    private AlertDialog AskOption() {
        final EditText input = new EditText(ClientActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Details")
                .setMessage("insert your location")


                .setPositiveButton("Add Location", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int whichButton) {
                        theClientLocation="";

                        if (!TextUtils.isEmpty(input.getText().toString())) {
                            theClientLocation = input.getText().toString();
                            in_the_cart_list = true;
                            ListView1.setVisibility(View.GONE);
                            searchtxt.setVisibility(View.GONE);
                            searchbtn.setVisibility(View.GONE);
                            send_order_btn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ClientActivity.this, "Enter the location or turn your location on ", Toast.LENGTH_SHORT).show();
                            inmenu = true;
                            back_btn.setVisibility(View.VISIBLE);
                            cart_btn.setVisibility(View.VISIBLE);
                            cartList.setVisibility(View.VISIBLE);
                            ordertxt.setVisibility(View.VISIBLE);

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


    ///// getting the location
    public void trackLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ClientActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        if (isPermissionToReadGPSLocationOK()) {
            // display Last Known Location
            showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            // start track GPS location as soon as possible or location changed
            long minTime = 0;       // minimum time interval between location updates, in milliseconds
            float minDistance = 0;  // minimum distance between location updates, in meters
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance,ClientActivity.this);

        }
        else
        {
            Toast.makeText(this, "NO GPS or Location Permission!", Toast.LENGTH_SHORT).show();
            theClientLocation="";
        }



    }

    private boolean isPermissionToReadGPSLocationOK()
    {
        // first, check if GPS Provider is Enabled ?
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            // second, check if permission to ACCESS_FINE_LOCATION is granted ?
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(ClientActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            Toast.makeText(this, "Turn on your location", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void onLocationChanged(Location location)
    {
        // Called when a new location is found by the location provider.
        showLocation(location);
    }

    public void onStatusChanged(String s, int i, Bundle bundle)
    {
    }

    public void onProviderEnabled(String s)
    {
    }

    public void onProviderDisabled(String s)
    {
    }


    private void showLocation(Location location)
    {
        if (location != null)
        {

            Geocoder geocoder = new Geocoder(ClientActivity.this, Locale.getDefault());
            List<Address> addresses = new ArrayList<Address>();
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception ioException) {
                Log.e(">>>>", "Error in getting address for the location");
            }
            if (addresses.size() > 0) {
                theClientLocation="";
                theClientLocation += addresses.get(0).getThoroughfare() + " " + addresses.get(0).getFeatureName() + " " + addresses.get(0).getLocality();
            }
        }

        else{
            AlertDialog diaBox = AskOption();
            diaBox.show();
        }
    }



}
