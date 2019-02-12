package com.example.rodrigo.travelly.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class Trip implements Parcelable {
    private int id;
    private String name;
    private Date startDate;
    private Vehicle vehicle;
    private LatLng originPosition;
    private LatLng destinationPosition;
    private ArrayList<Waypoint> waypoints;
    private ArrayList<Expense> expenses;
    private ArrayList<Reminder> reminders;
    private String imgPath;


    public Trip(String name, Date startDate, Vehicle vehicle, LatLng originPosition, LatLng destinationPosition) {
        // Overloading of constructor, if image path not specified, it will call as empty String
        this(name, startDate, vehicle, originPosition, destinationPosition, "");
    }

    public Trip(String name, Date startDate, Vehicle vehicle, LatLng originPosition, LatLng destinationPosition,String imgPath) {
        this.name = name;
        this.startDate = startDate;
        this.vehicle = vehicle;
        this.originPosition = originPosition;
        this.destinationPosition = destinationPosition;
        this.waypoints = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.imgPath = imgPath;
    }

    public Trip(){
        this.waypoints = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.imgPath = "";
    }

    public com.google.maps.model.LatLng[] getWaypointsForDirections(){
        // Function used to convert the waypoints to the appropriate structure to be used as waypoints in the directions API
        //Returns an array of objects of LatLng
        com.google.maps.model.LatLng[] formattedWaypoints = new com.google.maps.model.LatLng[waypoints.size()];

        int index = 0;
        for (Waypoint wp :
                waypoints) {
            com.google.maps.model.LatLng fwp = new com.google.maps.model.LatLng(wp.getPosition().latitude,wp.getPosition().longitude);
            formattedWaypoints[index] =  fwp;
            index++;
        };

        return formattedWaypoints;

    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }



    protected Trip(Parcel in) {
        id = in.readInt();
        name = in.readString();
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        originPosition = in.readParcelable(LatLng.class.getClassLoader());
        destinationPosition = in.readParcelable(LatLng.class.getClassLoader());
        waypoints = in.createTypedArrayList(Waypoint.CREATOR);
        reminders = in.createTypedArrayList(Reminder.CREATOR);
        expenses = in.createTypedArrayList(Expense.CREATOR);
        startDate = new Date(in.readLong());
        imgPath = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public void addWaypoint(Waypoint waypoint){
        if(this.waypoints == null)
            this.waypoints = new ArrayList<>();

        this.waypoints.add(waypoint);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LatLng getOriginPosition() {
        return originPosition;
    }

    public void setOriginPosition(LatLng originPosition) {
        this.originPosition = originPosition;
    }

    public LatLng getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(LatLng destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(vehicle, flags);
        dest.writeParcelable(originPosition, flags);
        dest.writeParcelable(destinationPosition, flags);
        dest.writeTypedList(waypoints);
        dest.writeTypedList(reminders);
        dest.writeTypedList(expenses);
        dest.writeLong(startDate.getTime());
        dest.writeString(imgPath);
    }
}
