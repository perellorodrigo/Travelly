package com.example.rodrigo.travelly.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.fragments.AccountFragment;
import com.example.rodrigo.travelly.fragments.AddTripFragment;
import com.example.rodrigo.travelly.fragments.HomeFragment;
import com.example.rodrigo.travelly.fragments.VehicleFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    private DatabaseHelper databaseHelper;

    //Bottom navigation menu
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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

        // Create shared preferences instance
        sp = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        // Check the user that is logged
        int userID = sp.getInt("activeUserID",-1);

        if(userID == -1){
            Toast.makeText(this,"Problem finding user",Toast.LENGTH_SHORT).show();
            //TODO Redirect to login page and erase all shared preferences variables
        }

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        //Setup static class of current logged in user
        AppData.loggedUserID = userID;
        AppData.loggedUserVehicles = databaseHelper.getVehiclesByUserID(userID);
        AppData.loggedUserTrips = databaseHelper.getTripsByUser(userID);

        //Setup default bottom navigation selected action
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
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
