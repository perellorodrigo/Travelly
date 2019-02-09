package com.example.rodrigo.travelly.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment extends Fragment {
    Button addNewTrip;
    EditText tripStartDate;
    EditText tripName;
    Spinner tripVehicleDropdown;
    Button setOriginBtn, setDestinationBtn;
    TextView originLocationText, destinationLocationText;
    int ORIGIN_PLACE_PICKER_REQUEST = 1, DESTINATION_PLACE_PICKER_REQUEST = 2;
    private DatabaseHelper databaseHelper;
    LatLng originLocation, destinationLocation;
    final Calendar myCalendar = Calendar.getInstance();


    public AddTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_add_trip,container,false);

        tripName = view.findViewById(R.id.tripName);
        tripStartDate = view.findViewById(R.id.tripStartDate);
        tripVehicleDropdown = view.findViewById(R.id.tripVehicleDropdown);
        setOriginBtn = view.findViewById(R.id.setOriginButton);
        setDestinationBtn = view.findViewById(R.id.setDestinationButton);
        originLocationText = view.findViewById(R.id.originLocationText);
        destinationLocationText = view.findViewById(R.id.destinationLocationText);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tripStartDate.setText(AppData.dateFormat.format(myCalendar.getTime()));
            }

        };

        final int userID = AppData.loggedUserID;
        Log.d("USER_LOGGED","user_trip_fragment: " + userID);

        databaseHelper = new DatabaseHelper(this.getContext());
        List<Vehicle> vehiclesFromDB = databaseHelper.getVehiclesByUserID(userID);


        //  TODO Remove this console log lines
        if (vehiclesFromDB.isEmpty())
            Log.d("VEHICLE_SEARCH","Something went wrong");
        else
            Log.d("VEHICLE_SEARCH","Count of Vehicles: " + vehiclesFromDB.size());

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_spinner_dropdown_item, vehiclesFromDB);
        tripVehicleDropdown.setAdapter(adapter);

        addNewTrip = (Button) view.findViewById(R.id.addTripButton);

        tripStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validInput())
                {
                    try {
                        databaseHelper.addTrip(
                                new Trip(
                                        tripName.getText().toString(),
                                        AppData.dateFormat.parse(tripStartDate.getText().toString()),
                                        (Vehicle) tripVehicleDropdown.getSelectedItem(),
                                        originLocation,
                                        destinationLocation),
                                userID);
                        Toast.makeText(getContext(),"Inserted new trip!", Toast.LENGTH_SHORT).show();

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"Date parse Exception! ex:" + e, Toast.LENGTH_SHORT).show();
                    }

                }else
                    Toast.makeText(getContext(),"Invalid input!", Toast.LENGTH_SHORT).show();
            }
        });

        setOriginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), ORIGIN_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        setDestinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build((getActivity())), DESTINATION_PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ORIGIN_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(),data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                originLocationText.setText(place.getName());
                originLocation = place.getLatLng();
            }
        }else if(requestCode == DESTINATION_PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getContext(),data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                destinationLocationText.setText(place.getName());
                destinationLocation = place.getLatLng();
            }
        }
    }

    public boolean validInput(){
        if (originLocation == null || destinationLocation == null)
            return false;
        else if (tripName.getText().toString().equals(""))
            return false;
        else if (tripStartDate.getText().toString().equals(""))
            return false;

        return true;
    }

}
