package com.example.rodrigo.travelly.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.MapsHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.time.Instant;

public class TripMainActivity extends AppCompatActivity {

    TextView startDate;
    TextView totalDistance;
    TextView tripTitle;
    Trip selectedTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        startDate = findViewById(R.id.startDate);
        tripTitle = findViewById(R.id.tripTitle);
        totalDistance = findViewById(R.id.totalDistance);


        Bundle data = this.getIntent().getExtras();
        selectedTrip = data.getParcelable("selectedTrip");

        if (selectedTrip != null)
        {
            //Get the selected trip in intent
            AppData.selectedTrip = selectedTrip; //Update the selected trip
            tripTitle.setText(selectedTrip.getName());
            startDate.setText("Date: "+ android.text.format.DateFormat.format("dd/MM/yy", selectedTrip.getStartDate()));
        }
        else{
            //If cannot find the selected trip, go to main screen
            Toast.makeText(this,"Couldn't find your trip",Toast.LENGTH_SHORT).show();
            Intent mainActivityIntent = new Intent(this,MainActivity.class);
            startActivity(mainActivityIntent);
        }

        try {
            if(MapsHelper.getRoute(selectedTrip)) {
                Toast.makeText(this,"Distance: "+ MapsHelper.tripDirections.routes[0].legs[0].distance.inMeters,Toast.LENGTH_SHORT).show();
                totalDistance.setText(MapsHelper.tripDirections.routes[0].legs[0].distance.humanReadable);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void OpenMap(View view) {
        //Open map action clicked
        Intent mapActivityIntent = new Intent(this,MapsActivity.class);
        startActivity(mapActivityIntent);
    }

    public void OpenSchedule(View view) {
        Toast.makeText(this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
    }


    public void OpenBudget(View view) {
        Toast.makeText(this,"Budget Clicked",Toast.LENGTH_SHORT).show();
        Intent budgetActivityIntent = new Intent(this,BudgetActivity.class);
        startActivity(budgetActivityIntent);
    }

    public void OpenNotes(View view) {
        Toast.makeText(this,"Notes Clicked",Toast.LENGTH_SHORT).show();
        Intent ReminderActivityIntent = new Intent(this,ReminderActivity.class);
        startActivity(ReminderActivityIntent);
    }

}
