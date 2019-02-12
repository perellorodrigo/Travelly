package com.example.rodrigo.travelly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;

public class LoginActivity extends AppCompatActivity {

    Button registerButton,loginButton;
    EditText emailText, passwordText;
    SharedPreferences sp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        // TODO Delete this line, debug purposes only
        //this.getSharedPreferences("login", 0).edit().clear().apply();

        //Link layout attributes
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Create the Login shared preferences instance
        sp = getSharedPreferences("login",MODE_PRIVATE);

        //If there is already an user logged in, it will go to the main activity
        if(sp.getBoolean("logged",false))
            goToMainActivity();

        //Create a new database instance to access login details
        databaseHelper = new DatabaseHelper(this);

        String email;
        //Get intent that called this screen
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //If this screen is called from register or logout, it will fill the user email field
        if(extras != null) {
            if (extras.containsKey("email")) {
                email = intent.getStringExtra("email");
                emailText.setText(email);
            }
        }
    }


    public void Login(View view) {
        // First check if user exists
        if(databaseHelper.checkUser(emailText.getText().toString()))
        {
            //If user exists, check if the password matches
            if (databaseHelper.checkUser(emailText.getText().toString(),passwordText.getText().toString()))
            {
                // get the user details that login
                AppData.loggedUser = databaseHelper.getUserByEmail(emailText.getText().toString());
                if(AppData.loggedUser != null)
                {
                    // Fill the details in the sharedPreferences
                    //This values will be used to check if user is logged in even if the application is closed
                    sp.edit().putInt("activeUserID",AppData.loggedUser.getId()).apply();
                    sp.edit().putBoolean("logged",true).apply();
                    // Go to main activity
                    goToMainActivity();
                }
                else
                    Toast.makeText(this,"Couldn't find the user",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Username and password don't match",Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(this,"User don't exist", Toast.LENGTH_SHORT).show();
    }

    public void Register(View view) {
        Intent registerActivityIntent = new Intent(this,RegisterActivity.class);
        registerActivityIntent.putExtra("email",emailText.getText().toString());
        startActivity(registerActivityIntent);
    }
    public void goToMainActivity(){
        //When the user logs in successfully
        Intent mainActivityIntent = new Intent(this,MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}
