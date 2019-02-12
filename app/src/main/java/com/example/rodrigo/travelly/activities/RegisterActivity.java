package com.example.rodrigo.travelly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.User;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton,loginButton;
    EditText nameText,passwordText,emailText;
    private DatabaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Create Account");

        // Link Activity to layout components:
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        //Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Create new user
        user = new User();


        //If the email field was filled in the login before calling register, it will fill the same value
        String email;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey("email")) {
                email = intent.getStringExtra("email");
                emailText.setText(email);
            }
        }

    }

    public void Register(View view) {

        user.setName(nameText.getText().toString());
        user.setEmail(emailText.getText().toString());
        user.setPassword(passwordText.getText().toString());

        if (databaseHelper.checkUser(user.getEmail()))
            Toast.makeText(this,"There is already an user registered with this email",Toast.LENGTH_SHORT).show();
        else if (user.getEmail().length() < 6)
            Toast.makeText(this,"Email to short",Toast.LENGTH_SHORT).show();
        else{
            //Here the security checks are done and successful, try to insert user in Database

            user = databaseHelper.addUser(user);
            if (user == null) {
                Toast.makeText(this, "Something went wrong while creating your account!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();


            //Sample vehicle added to user account
            Vehicle sampleVehicle = databaseHelper.addVehicle(new Vehicle(
                    "Toyota Hilux",
                    8.0f
            ),user.getId());


            LatLng l1 = new LatLng(-33.919987, 151.251037);
            LatLng l2 = new LatLng(-33.884324, 151.238829);

            databaseHelper.addTrip(new Trip(
                    "Sample Trip",
                    new Date(2020,11,2),
                    sampleVehicle,
                    l1,
                    l2
            ),user.getId());


            Intent loginActivityIntent = new Intent(this,LoginActivity.class);
            loginActivityIntent.putExtra("email",emailText.getText().toString());
            startActivity(loginActivityIntent);

        }
    }

    public void Login(View view) {
        Intent loginActivityIntent = new Intent(this,LoginActivity.class);
        loginActivityIntent.putExtra("email",emailText.getText().toString());
        startActivity(loginActivityIntent);
    }
}
