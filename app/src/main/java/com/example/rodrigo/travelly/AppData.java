package com.example.rodrigo.travelly;

import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.maps.model.DirectionsResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AppData {
    public static int loggedUserID;
    public static ArrayList<Vehicle> loggedUserVehicles;
    public static ArrayList<Trip> loggedUserTrips;
    public static Trip selectedTrip;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

}
