package com.example.rodrigo.travelly.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

public class Waypoint implements Parcelable {
    private int id;
    private String description;
    private Date expectedDate;
    private LatLng position;


    public Waypoint(){
    //Empty constructor, useful for database creation
    }

    public Waypoint(String description, Date expectedDate, LatLng position) {
        this.description = description;
        this.expectedDate = expectedDate;
        this.position = position;
    }

    protected Waypoint(Parcel in) {
        id = in.readInt();
        description = in.readString();
        position = in.readParcelable(LatLng.class.getClassLoader());
        expectedDate = new Date(in.readLong());
    }

    public static final Creator<Waypoint> CREATOR = new Creator<Waypoint>() {
        @Override
        public Waypoint createFromParcel(Parcel in) {
            return new Waypoint(in);
        }

        @Override
        public Waypoint[] newArray(int size) {
            return new Waypoint[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeParcelable(position, flags);
        dest.writeLong(expectedDate.getTime());
    }
}
