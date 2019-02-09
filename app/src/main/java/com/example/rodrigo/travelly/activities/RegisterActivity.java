package com.example.rodrigo.travelly.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.User;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton,loginButton;
    EditText nameText,hometownText,passwordText,emailText;
    private DatabaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Link Activity to layout components:
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        nameText = findViewById(R.id.nameText);
        hometownText = findViewById(R.id.hometownText);
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
                email = ((Intent) intent).getStringExtra("email");
                emailText.setText(email);
            }
        }

    }

    public void Register(View view) {

        //TODO Security checks: Password Size, email not null

        user.setName(nameText.getText().toString());
        user.setEmail(emailText.getText().toString());
        user.setHometown(hometownText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        AppData.loggedUserVehicles = new ArrayList<>();
        AppData.loggedUserTrips = new ArrayList<>();

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
                Toast.makeText(this, "Account created successfully, User ID: " + user.getId(), Toast.LENGTH_SHORT).show();


            //Sample vehicle added to user account
            Vehicle demoVehicle = databaseHelper.addVehicle(new Vehicle(
                    "Sample Vehicle - " + user.getId(),
                    8.0f
            ),user.getId());
            if (demoVehicle == null) {
                Toast.makeText(this, "Database error while inserting Vehicle!", Toast.LENGTH_SHORT).show();
            }

            ArrayList<Vehicle> vehiclesFromDB = databaseHelper.getVehiclesByUserID(user.getId());
            if (vehiclesFromDB.isEmpty()) {
                Toast.makeText(this, "Couldn't find the vehicle", Toast.LENGTH_SHORT).show();
                Log.d("VEHICLE_FIND_OP","Couldn't find");
            }
            else
            {
                Toast.makeText(this,"Found this guy: " + vehiclesFromDB.get(0).getVehicleName(),Toast.LENGTH_SHORT).show();
                Log.d("VEHICLE_FIND_OP","Found It");
            }

            LatLng l1 = new LatLng(-33.919987, 151.251037);
            LatLng l2 = new LatLng(-33.884324, 151.238829);


            databaseHelper.addTrip(new Trip(
                    "Demo Trip",
                    new Date(2020,11,2),
                    demoVehicle,
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
