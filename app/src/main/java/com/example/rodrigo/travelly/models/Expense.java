package com.example.rodrigo.travelly.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Expense implements Parcelable {
    private int id;
    private String description;
    private float amount;
    private Date date;


    public Expense(String description, float amount, Date date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected Expense(Parcel in) {
        id = in.readInt();
        amount = in.readFloat();
        description = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(amount);
        dest.writeString(description);
        dest.writeLong(date.getTime());
    }
}
