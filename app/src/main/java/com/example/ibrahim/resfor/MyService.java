package com.example.ibrahim.resfor;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    final String CHANNEL_ID="ORDER_RECIEVED";
    private NotificationManager notificationManager;
    private boolean newOrder=false;
    final int ID=1;
    private int howManyOrders=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        rootRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        final String userID = auth.getCurrentUser().getUid();
        //Log.d("ssdd", "onDataChange: ");

        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "Simple Notification",
                            NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        rootRef.child("Users").child(userID).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("Restaurant")){
                    //montaser..
                    rootRef.child("Users").child(userID).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("service", "onDataChange: ");
                            howManyOrders=0;
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                howManyOrders++;
                            }
                            if(howManyOrders>0){
                                newOrder=false;
                                notificate(ID,"New Order Recieved","You have "+howManyOrders+" orders!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }else{
                    //ibrahim..
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return START_STICKY;
    }

    private void notificate(int id,String title,String Text){
        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_orders)
                        .setContentTitle(title)
                        .setContentText(Text)
                        .build();
        notificationManager.notify(id, notification);

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        //stopping the player when service is destroyed
     /*   player.stop();
        Log.d("debug","MyService onDestroy()");
        isRunning = false;*/
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
