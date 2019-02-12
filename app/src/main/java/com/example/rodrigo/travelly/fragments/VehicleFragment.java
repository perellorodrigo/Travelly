package com.example.rodrigo.travelly.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.adapters.VehicleAdapter;
import com.example.rodrigo.travelly.models.Vehicle;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleFragment extends Fragment {

    Button addNewVehicleButton;
    ListView list;
    VehicleAdapter adapter;
    ArrayList<Vehicle> vehicles;
    EditText vehicleName;
    EditText fuelConsumption;
    Vehicle selectedVehicle;
    private DatabaseHelper databaseHelper;

    public VehicleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicle,container,false);
        vehicles = AppData.loggedUserVehicles;
        list = view.findViewById(R.id.vehicleListView);
        addNewVehicleButton = view.findViewById(R.id.addVehicleButton);
        vehicleName = view.findViewById(R.id.vehicleName);
        fuelConsumption = view.findViewById(R.id.fuelConsumption);
        databaseHelper = new DatabaseHelper(getContext());

        adapter = new VehicleAdapter(inflater.getContext(),vehicles);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicle = adapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Would you like to delete this vehicle?")
                        .setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();
                return false;
            }
        });

        addNewVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Vehicle newVehicle = databaseHelper.addVehicle(new Vehicle(
                                                                vehicleName.getText().toString(),
                                                                Float.parseFloat(fuelConsumption.getText().toString())),
                                                            AppData.loggedUser.getId());
                if (newVehicle != null){
                    Toast.makeText(getContext(),"Successfully added " + vehicleName.getText().toString(),Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(getContext(),"Couldn't add new Vehicle" + vehicleName.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if (databaseHelper.deleteVehicleByID(selectedVehicle.getId()))
                        Toast.makeText(getContext(),"Deleted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(),"Please delete the trips associated with this vehicle", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };



}
