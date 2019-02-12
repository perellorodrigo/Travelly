package com.example.rodrigo.travelly.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.MapsHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.fragments.AddWaypointFragment;
import com.example.rodrigo.travelly.models.Waypoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AddWaypointFragment.AddWaypointDialogListener {

    private GoogleMap mMap;
    private DatabaseHelper databaseHelper;
    Context context;
    LatLng waypointPosition;
    Polyline tripPath;
    ArrayList<Marker> tripMarkers;
    Intent returnIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Trip Map");
        // Initialize Array List
        tripMarkers = new ArrayList<>();

        // Instantiate database helper context
        databaseHelper = new DatabaseHelper(this);

        // Save context to use anywhere in the activity
        context = this;

        returnIntent = new Intent();
        //The default return is result canceled, which means that no waypoints have been added
        setResult(Activity.RESULT_CANCELED, returnIntent);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    //Confirm dialog interface for waypoint
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                FragmentManager fm = getSupportFragmentManager();
                AddWaypointFragment addWaypointFragment = AddWaypointFragment.newInstance();
                addWaypointFragment.show(fm,"add_description");
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //This function is called when the map is ready
        mMap = googleMap;

        //Personalized window to show marker details
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                //Window Title
                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                //Snippet text view
                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                //Add layouts
                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //Add trip markers
        addTripMarkers();

        //When map finishes loading, it will adjust the bounds according to trip final and destination locations
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // LatLng Bounds builder, the map camera will fit everything added to this builder (Dynamic focus)
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(AppData.selectedTrip.getOriginPosition());
                builder.include(AppData.selectedTrip.getDestinationPosition());

                //Move the camera to fit the destination and origin locations
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
                //Add the directions of the Trip
                addPolyline(MapsHelper.tripDirections);
            }
        });

        // Map click listener to add the waypoints
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                waypointPosition = latLng;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to insert a waypoint in this location?")
                        .setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();
            }
        });

    }

    private void addTripMarkers() {

        //Remove all markers
        for (Marker marker : tripMarkers){
            marker.remove();
        }

        //Put Origin Marker on Map
        tripMarkers.add(mMap.addMarker(new MarkerOptions().position(AppData.selectedTrip.getOriginPosition())
                .title("Origin")));
        //Put Destination Marker on Map
        tripMarkers.add(mMap.addMarker(new MarkerOptions()
                .position(AppData.selectedTrip.getDestinationPosition())
                .title("Destination")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

        //Put waypoints on Map
        if(!AppData.selectedTrip.getWaypoints().isEmpty())
            for (Waypoint wp :
                    AppData.selectedTrip.getWaypoints()) {
                tripMarkers.add(mMap.addMarker(new MarkerOptions()
                        .position(wp.getPosition())
                        .title("Waypoints").snippet(wp.getDescription() + "\nDate: " + wp.getExpectedDate())
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_marker_v3, Integer.toString(wp.getOrder()))))));
            }
    }

    private void addPolyline(DirectionsResult results) {
        //TODO next versions: create colored path for each leg of the trip
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        tripPath = mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(R.attr.colorPrimary));
    }

    @Override
    public void onFinishEditDialog(Waypoint wp) {
        //After inserting the waypoint details:

        //Set position of waypoint
        wp.setPosition(waypointPosition);

        //Add waypoint to database
        wp = databaseHelper.addWaypoint(wp, AppData.selectedTrip.getId());
        if(wp != null)
        {
            //If added:

            //Add to the selectedTrip static class
            AppData.selectedTrip.addWaypoint(wp);
            try {
                if(MapsHelper.getRoute(AppData.selectedTrip,true)) // Update the Trip route, after running this function the route will include the last added waypoint
                {
                    //Remove current Trip path
                    tripPath.remove();
                    //Add updated Trip path
                    addPolyline(MapsHelper.tripDirections);
                    //Set activity result to RESULT_OK -> Added waypoint
                    setResult(Activity.RESULT_OK,returnIntent);
                    //Add trip markers to map
                    addTripMarkers();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this,"Waypoint is null",Toast.LENGTH_SHORT).show();
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {
        //Function to draw custom marker on map
        //
        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(context, 16));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(context, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }
    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }
}
