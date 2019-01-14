package com.example.ibrahim.resfor.Restaurant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibrahim.resfor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference rootRef,itemRef;
    private ImageView image;
    private EditText name,price,des;
    private Button add;
    Uri selectedImageUri;
    public static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        rootRef= FirebaseDatabase.getInstance().getReference();
        itemRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        image=findViewById(R.id.itemAddImage);
        name=findViewById(R.id.itemAddName);
        price=findViewById(R.id.itemAddPrice);
        des=findViewById(R.id.itemAddDes);
        add=findViewById(R.id.AddItemBtn);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndroidVersion();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageStr="";
                String nameStr=name.getText().toString();
                String priceStr=price.getText().toString();
                String desStr=des.getText().toString();
                if(TextUtils.isEmpty(nameStr)){
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(priceStr)){
                    Toast.makeText(getApplicationContext(), "Enter price!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(desStr)){
                   desStr=" ";
                }

               applyAdd2(imageStr,nameStr,priceStr,desStr);
                finish();
            }
        });



    }

    public void applyAdd2(final String image, final String name, final String price, final String description){
        final String userID = auth.getCurrentUser().getUid();
        DatabaseReference itemKeyRef=rootRef.child("Users").child(userID).child("menu").push();
        final String itemKey = itemKeyRef.getKey();
        itemRef = rootRef.child("Users").child(userID).child("menu");
        HashMap items =new HashMap();

        //StorageReference filePath = userProfileImagesRef.child(currentUserID + ".jpg");
        //FirebaseStorage storage = FirebaseStorage.getInstance();
       // StorageReference storageRef = userProfileImagesRef.child;

        items.put("name",name);
        items.put("price",price);
        items.put("description",description);
        items.put("image",image);
      /*  rootRef.child("Users").child(userID).child("menu").child(itemKey).child("name").setValue(name);
        rootRef.child("Users").child(userID).child("menu").child(itemKey).child("price").setValue(price);
        rootRef.child("Users").child(userID).child("menu").child(itemKey).child("description").setValue(description);
        rootRef.child("Users").child(userID).child("menu").child(itemKey).child("image").setValue(image);*/
        rootRef.child("Users").child(userID).child("menu").child(itemKey).updateChildren(items).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    //startActivity(new Intent(AddItemActivity.this, MenuActivity.class));

                   // Toast.makeText(MenuActivity.class, "Item added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddItemActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");
        if(selectedImageUri != null) {


            final StorageReference childRef = storageRef.child(userID).child(itemKey+".jpg");
            childRef.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddItemActivity.this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                        //get the link of the profile image from the storage and store the link in the database
                      //  Log.d(">>>>", "onComplete: " + task.getResult().getStorage().getDownloadUrl().toString());
                       // final String downloadUri = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                       // Log.d(">>>", "onComplete: "+downloadUri);
                        task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                  Log.d(">>>>", "onComplete: " + uri);
                                final String downloadUri = uri.toString();
                                rootRef.child("Users").child(userID).child("menu").child(itemKey).child("image").setValue(downloadUri)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddItemActivity.this, "Image saved in the database", Toast.LENGTH_SHORT).show();
                                                    // pick=false;
                                                } else {
                                                    // userName.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        });
                            }
                        });

                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(AddItemActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }

    private void checkAndroidVersion(){
        //REQUEST PERMISSION
        if (ActivityCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);


        } else {
            Log.d(">>>>>", "checkAndroidVersion: ");
            pickImage();
        }
    }

    public void pickImage() {
        //CropImage.startPickImageActivity(this);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
             selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);


                Log.d(">>>", "onActivityResult: ");
            }
            image.setImageURI(selectedImageUri);
        }
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
