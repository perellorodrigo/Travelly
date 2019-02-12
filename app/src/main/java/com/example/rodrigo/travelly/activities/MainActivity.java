package com.example.rodrigo.travelly.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.fragments.AccountFragment;
import com.example.rodrigo.travelly.fragments.AddTripFragment;
import com.example.rodrigo.travelly.fragments.HomeFragment;
import com.example.rodrigo.travelly.fragments.VehicleFragment;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    private DatabaseHelper databaseHelper;

    //Bottom navigation menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Implementation of the bottom navigation, each screen is a fragment
            Fragment fragment = null;
            // Switch case for the option selected
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_vehicle:
                    fragment = new VehicleFragment();
                    break;
                case R.id.navigation_account:
                    fragment = new AccountFragment();
                    break;
                case R.id.navigation_addTrip:
                    fragment = new AddTripFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load shared preferences
        sp = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        // Check the user that is logged
        int userID = sp.getInt("activeUserID",-1);
        AppData.loggedUser = databaseHelper.getUserByID(userID);

        if(userID == -1 || !sp.getBoolean("logged",false) || AppData.loggedUser == null){
            //If the user couldn't be found, it will redirect to the login screen
            Toast.makeText(this,"Problem finding user",Toast.LENGTH_SHORT).show();
            sp.edit().putBoolean("logged",false).apply();
            Intent loginActivityIntent = new Intent(this,LoginActivity.class);
            startActivity(loginActivityIntent);
            //Finish the activity, preventing user to going back to this screen using back button
            finish();
        }


        // Initialize database helper


        //Setup static properties of current logged user details
        AppData.loggedUserVehicles = databaseHelper.getVehiclesByUserID(userID);
        AppData.loggedUserTrips = databaseHelper.getTripsByUser(userID);

        //Setup default bottom navigation selected action
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        //Loads home fragment
        loadFragment(new HomeFragment());

    }

    private boolean loadFragment(Fragment fragment) {
        // load fragment
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
