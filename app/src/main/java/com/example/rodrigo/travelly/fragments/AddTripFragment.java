package com.example.rodrigo.travelly.fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripFragment extends Fragment {
    // Layout elements
    Button addNewTrip;
    EditText tripStartDate;
    EditText tripName;
    Spinner tripVehicleDropdown;
    ImageView imageView;

    // Trip image path and Bitmap:
    String tripImagePath = "";
    Bitmap tripImageBitmap;


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
        // Link layout elements
        tripName = view.findViewById(R.id.tripName);
        tripStartDate = view.findViewById(R.id.tripStartDate);
        tripVehicleDropdown = view.findViewById(R.id.tripVehicleDropdown);
        imageView = view.findViewById(R.id.imageView);
        addNewTrip = view.findViewById(R.id.addTripButton);
        //

        // Get logged userID
        final int userID = AppData.loggedUser.getId();

        // Instantiate database helper
        databaseHelper = new DatabaseHelper(this.getContext());

        // Get Vehicles from database and populate dropdown
        List<Vehicle> vehiclesFromDB = databaseHelper.getVehiclesByUserID(userID);
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_spinner_dropdown_item, vehiclesFromDB);
        tripVehicleDropdown.setAdapter(adapter);
        //


        // Set button click listeners
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
                        if(tripImageBitmap != null)
                            tripImagePath = saveDownloadedImage(tripImageBitmap);
                        else
                            tripImagePath = "";

                        //Add new trip to the database
                        databaseHelper.addTrip(
                                new Trip(
                                        tripName.getText().toString(),
                                        AppData.dateFormat.parse(tripStartDate.getText().toString()),
                                        (Vehicle) tripVehicleDropdown.getSelectedItem(),
                                        originLocation,
                                        destinationLocation,
                                        tripImagePath),
                                userID);
                        Toast.makeText(getContext(),"Inserted new trip!", Toast.LENGTH_SHORT).show();

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"Date parse Exception! ex:" + e, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getContext(),"Invalid input!", Toast.LENGTH_SHORT).show();
            }
        });
        //-----


        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyC3jioXtoNAJ8Sua_ORvo7eQAzfE8D6GHY");
        }

        // Initialize the AutocompleteSupportFragment for Origin
        AutocompleteSupportFragment originAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_origin);

        // Set requested fields for origin
        originAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME));

        // Set callback function after origin place is chosen
        originAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getContext(),"Place: " + place.getName() + ", " + place.getId(),Toast.LENGTH_SHORT).show();

                // Set the origin location to selected place latitude and longitude
                originLocation = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(),"Couldn't select the Place",Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the AutocompleteSupportFragment for Destination
        AutocompleteSupportFragment destinationAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_destination);

        // Set requested fields for destination
        destinationAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME,Place.Field.PHOTO_METADATAS));

        // Set callback function after destination place is chosen
        destinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Toast.makeText(getContext(),"Place: " + place.getName() + ", " + place.getId(),Toast.LENGTH_SHORT).show();

                // Set the destination location to selected place latitude and longitude
                destinationLocation = place.getLatLng();

                // Get the PhotoMetadata  which will be used to request picture of the place through API

                PhotoMetadata photoMetadata;
                if (place.getPhotoMetadatas() != null)
                    photoMetadata = place.getPhotoMetadatas().get(0);
                else
                {
                    Toast.makeText(getContext(), "Couldn't find any image",Toast.LENGTH_SHORT).show();
                    tripImageBitmap = null;
                    return;
                }

                // Request the place picture from Directions API
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();

                //Create API client request object
                PlacesClient placesClient = Places.createClient(getContext());

                //After request has been received
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener((fetchPhotoResponse) -> { //Successfully received:
                        tripImageBitmap = fetchPhotoResponse.getBitmap();
                        imageView.setImageBitmap(tripImageBitmap);
                    })
                    .addOnFailureListener((exception) -> { // Failure:
                        if (exception instanceof ApiException) {
                            tripImageBitmap = null;
                        }
                    });
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getContext(),"Error ",Toast.LENGTH_SHORT).show();
            }
        });
        // Finished destination place picker
        //-------------------

        return view;
    }

    private String saveDownloadedImage(Bitmap bmp) {
        //Save image as a encoded string to store it in the database
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imgBytes = baos.toByteArray();
            String base64String = Base64.encodeToString(imgBytes,
                    Base64.DEFAULT);

            //Return the encoded image as String
            return base64String;
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tripStartDate.setText(AppData.dateFormat.format(myCalendar.getTime()));
        }

    };

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
