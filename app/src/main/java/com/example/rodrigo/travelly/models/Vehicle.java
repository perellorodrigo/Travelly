package com.example.rodrigo.travelly.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable{
    private int id;
    private String vehicleName;
    private float fuelConsumption;

    public Vehicle(String vehicleName, float fuelConsumption) {
        this.vehicleName = vehicleName;
        this.fuelConsumption = fuelConsumption;
    }

    protected Vehicle(Parcel in) {
        id = in.readInt();
        vehicleName = in.readString();
        fuelConsumption = in.readFloat();
    }
    public String toString()
    {
        return( vehicleName);
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public float getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(float fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(vehicleName);
        dest.writeFloat(fuelConsumption);
    }
}
