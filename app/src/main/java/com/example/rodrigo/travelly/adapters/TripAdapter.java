package com.example.rodrigo.travelly.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;

import java.util.ArrayList;

// Adapter to display the list of trips for the user
public class TripAdapter extends ArrayAdapter<Trip> {
    // Constructor
    public TripAdapter(@NonNull Context context, ArrayList<Trip> trips) {
        super(context, R.layout.trip_row_layout,trips);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Get item position
        Trip trip = getItem(position);


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.trip_row_layout,parent,false);

        TextView destinationText = (TextView) view.findViewById(R.id.destinationText);
        TextView startDate = (TextView) view.findViewById(R.id.startDate);

        //Set Row layout attributes
        destinationText.setText(trip.getName());
        startDate.setText(android.text.format.DateFormat.format("dd/MM/yy", trip.getStartDate()));
        // TODO: Personalized image for each trip

        return view;

    }
}
