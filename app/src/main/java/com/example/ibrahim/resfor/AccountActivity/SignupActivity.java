package com.example.ibrahim.resfor.AccountActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.ibrahim.resfor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword,inputName,inputPhone,restaurant_location;
    private RadioButton client,rest;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth instance
        rootRef= FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        btnSignIn =  findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        progressBar =  findViewById(R.id.progressBar);
        inputName=findViewById(R.id.name);
        inputPhone=findViewById(R.id.phone);
        restaurant_location=findViewById(R.id.Restaurant_location);
        client=findViewById(R.id.Client);
        rest=findViewById(R.id.Restaurant);

        rest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                restaurant_location.setVisibility(View.VISIBLE);
                if(!b)
                restaurant_location.setVisibility(View.GONE);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String name=inputName.getText().toString();
                final String phone=inputPhone.getText().toString();
                final String type;
                final String res_location=restaurant_location.getText().toString();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(), "Enter phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(client.isChecked()){
                    type="Client";
                }
                else {
                    type="Restaurant";
                    if(TextUtils.isEmpty(res_location)){
                        Toast.makeText(getApplicationContext(), "Enter the restaurant city!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    final String userID = auth.getCurrentUser().getUid();
                                    rootRef.child("Users").child(userID).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                rootRef.child("Users").child(userID).child("email").setValue(email);
                                                rootRef.child("Users").child(userID).child("email").orderByValue();
                                                rootRef.child("Users").child(userID).child("name").setValue(name);
                                                rootRef.child("Users").child(userID).child("phone").setValue(phone);
                                                rootRef.child("Users").child(userID).child("password").setValue(password);
                                                rootRef.child("Users").child(userID).child("type").setValue(type);
                                                if(type.equals("Restaurant")){
                                                    rootRef.child("Users").child(userID).child("Restaurant_location").setValue(res_location);
                                                }
                                                auth.signOut();
                                                finish();
                                            }else{
                                                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
