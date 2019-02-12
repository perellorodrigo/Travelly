package com.example.rodrigo.travelly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.MapsHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.google.maps.errors.ApiException;

import java.io.IOException;

public class TripMainActivity extends AppCompatActivity {

    TextView startDate;
    TextView totalDistance;
    TextView tripTitle;
    Trip selectedTrip;
    private final int MAPS_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        // Link layout elements
        startDate = findViewById(R.id.startDate);
        tripTitle = findViewById(R.id.tripTitle);
        totalDistance = findViewById(R.id.totalDistance);
        //-----

        // Get intent data
        //Bundle data = this.getIntent().getExtras();

        //selectedTrip = data.getParcelable("selectedTrip");
        selectedTrip = AppData.selectedTrip;

        //Get selected Trip

        if (selectedTrip != null)
        {
            //Get the selected trip in intent
            setTitle(selectedTrip.getName());
            tripTitle.setText(selectedTrip.getName());
            startDate.setText("Date: "+ android.text.format.DateFormat.format("dd/MM/yy", selectedTrip.getStartDate()));
        }
        else{
            //If cannot find the selected trip, go to main screen
            Toast.makeText(this,"Couldn't find your trip",Toast.LENGTH_SHORT).show();
            Intent mainActivityIntent = new Intent(this,MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }

        try {
            if(MapsHelper.getRoute(selectedTrip))
                totalDistance.setText("Length: " + MapsHelper.tripDistanceAsString); //Display trip Length
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MAPS_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //If the trip was updated -> added waypoints
                totalDistance.setText("Length: " + MapsHelper.tripDistanceAsString);
            }
        }
    }

    public void OpenMap(View view) {
        //Open map action clicked
        Intent mapActivityIntent = new Intent(this,MapsActivity.class);
        startActivityForResult(mapActivityIntent,MAPS_ACTIVITY);
    }

    public void OpenSchedule(View view) {
        //TODO next versions implement Schedule
        Toast.makeText(this,"Schedule To be implemented in next versions",Toast.LENGTH_LONG).show();
    }


    public void OpenBudget(View view) {
        Intent budgetActivityIntent = new Intent(this,BudgetActivity.class);
        startActivity(budgetActivityIntent);
    }

    public void OpenReminder(View view) {
        //Open Trip Reminders
        Intent ReminderActivityIntent = new Intent(this,ReminderActivity.class);
        startActivity(ReminderActivityIntent);
    }

}
