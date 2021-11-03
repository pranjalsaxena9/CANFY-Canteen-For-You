package com.anchal.canteentest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anchal.canteentest.Common.Common;
import com.anchal.canteentest.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity
{

    EditText phone_editText;
    EditText password_editText;

    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phone_editText = (EditText) findViewById(R.id.phone_editText);
        password_editText = (EditText) findViewById(R.id.password_ediText);

        signIn = (Button)findViewById(R.id.signIn);


        // Getting reference of database .
        // This database is associated with firebase account of email account LIT2019007@iiitl.ac.in
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-app-36b01-default-rtdb.asia-southeast1.firebasedatabase.app");
        final DatabaseReference customer = database.getReference("User");



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT);


                customer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(phone_editText.getText().toString()).exists()) {
                            User user = dataSnapshot.child(phone_editText.getText().toString()).getValue(User.class);
                            user.setPhoneNo(phone_editText.getText().toString());
                            if (user.getPassword().equals(password_editText.getText().toString())) {
                                Intent homeIntent = new Intent(SignIn.this, HomeActivity.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {

                                // If the password is incorrect then we will show a toast to the user .
                                Toast.makeText(SignIn.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            // Checking if the user is registered or not, if not we show him a toast and ask him/ her to register first.
                            Toast.makeText(SignIn.this, "You're not registered yet. Sign up!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}