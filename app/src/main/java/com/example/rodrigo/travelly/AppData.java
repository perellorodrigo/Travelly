package com.example.rodrigo.travelly;

import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.User;
import com.example.rodrigo.travelly.models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AppData {
    //Keeps track of the current user
    public static User loggedUser;
    //Logged user Vehicles and trips
    public static ArrayList<Vehicle> loggedUserVehicles = new ArrayList<>();
    public static ArrayList<Trip> loggedUserTrips = new ArrayList<>();
    //Stores the current selected Trip
    public static Trip selectedTrip;
    //Standard datetime used across the application
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

}
