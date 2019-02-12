package com.example.rodrigo.travelly.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Trip;

import java.util.ArrayList;

// Adapter to display the list of trips for the user
public class TripAdapter extends ArrayAdapter<Trip> {
    // Constructor
    private Context context;
    public TripAdapter(@NonNull Context context, ArrayList<Trip> trips) {
        super(context, R.layout.trip_row_layout,trips);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trip_row_layout,parent,false);

        try {
            Trip trip = getItem(position);

            ImageView tripImage = view.findViewById(R.id.tripImage);
            TextView destinationText = view.findViewById(R.id.destinationText);
            TextView startDate = view.findViewById(R.id.startDate);

            //Set Row layout attributes

            if (trip != null){
                if(!trip.getImgPath().equals("")) {
                    //If there is a image path, loads the image
                    Bitmap image = setImage(trip.getImgPath());
                    tripImage.setImageBitmap(image);
                }
                destinationText.setText(trip.getName());
                startDate.setText(android.text.format.DateFormat.format("dd/MM/yy", trip.getStartDate()));
            }

            return view;
        } catch (NullPointerException ex)
        {
            Toast.makeText(context, "Something went wrong while loading the trip",Toast.LENGTH_SHORT).show();
            return view;
        }

    }

    private Bitmap setImage(String base64String) {
        //Function to get the image, receives the encoded image as string and returns bitmap
        Bitmap bmp = null;
        try {
            if (base64String == null || base64String.equals("")) {
                Toast.makeText(context,"Couldn't find  the image", Toast.LENGTH_SHORT).show();
            } else {
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                bmp =  BitmapFactory.decodeByteArray(
                        decodedString, 0, decodedString.length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
