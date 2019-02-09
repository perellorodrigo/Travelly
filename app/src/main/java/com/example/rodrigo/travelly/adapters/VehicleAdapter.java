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
import com.example.rodrigo.travelly.models.Vehicle;

import java.util.ArrayList;

// Adapter to display the list of vehicles of the user
public class VehicleAdapter extends ArrayAdapter<Vehicle> {
    public VehicleAdapter(@NonNull Context context, ArrayList<Vehicle> vehicles) {
        super(context, R.layout.vehicle_row_layout, vehicles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get position of item
        Vehicle vehicle = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.vehicle_row_layout,parent,false);

        // Link layout components to this fragment
        TextView vehicleName = (TextView) view.findViewById(R.id.vehicleName);
        TextView fuelConsumption = (TextView) view.findViewById(R.id.fuelConsumption);

        //Set vehicle properties
        vehicleName.setText(vehicle.getVehicleName());
        fuelConsumption.setText(vehicle.getFuelConsumption() + "km/l");

        return view;

    }
}
