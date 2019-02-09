package com.example.rodrigo.travelly.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.MapsHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.fragments.AddWaypointFragment;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.Waypoint;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AddWaypointFragment.AddWaypointDialogListener {

    private GoogleMap mMap;
    private DatabaseHelper databaseHelper;
    Context context;
    LatLng waypointPosition;
    Polyline tripPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        databaseHelper = new DatabaseHelper(this);

        //Bundle data = this.getIntent().getExtras();
        //selectedTrip = (Trip) data.getParcelable("selectedTrip");
        //AppData.selectedTrip = selectedTrip;
        context = this;

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

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        addTripMarkers();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(AppData.selectedTrip.getOriginPosition());
                builder.include(AppData.selectedTrip.getDestinationPosition());

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
                addPolyline(MapsHelper.tripDirections);
            }
        });

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

        //Put Origin on Map
        mMap.addMarker(new MarkerOptions().position(AppData.selectedTrip.getOriginPosition())
                .title("Origin")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //Put Destination on Map
        mMap.addMarker(new MarkerOptions()
                .position(AppData.selectedTrip.getDestinationPosition())
                .title("Destination"));

        //Put waypoints on Map
        if(AppData.selectedTrip.getWaypoints() != null)
            for (Waypoint wp :
                    AppData.selectedTrip.getWaypoints()) {
                mMap.addMarker(new MarkerOptions()
                        .position(wp.getPosition())
                        .title("Waypoints").snippet(wp.getDescription() + "\nDate: " + wp.getExpectedDate())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
    }

    private void addPolyline(DirectionsResult results) {

        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        tripPath = mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    @Override
    public void onFinishEditDialog(Waypoint wp) {
        wp.setPosition(waypointPosition);
        wp = databaseHelper.addWaypoint(wp, AppData.selectedTrip.getId());
        if(wp != null)
        {
            AppData.selectedTrip.addWaypoint(wp);

            mMap.addMarker(new MarkerOptions()
                    .position(wp.getPosition())
                    .title("Waypoints").snippet(wp.getDescription() + "\nDate: " + wp.getExpectedDate())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            try {
                if(MapsHelper.getRoute(AppData.selectedTrip)){
                    tripPath.remove();
                    addPolyline(MapsHelper.tripDirections);
                    Toast.makeText(context,"Distance: "+ MapsHelper.tripDirections.routes[0].legs[0].distance.inMeters,Toast.LENGTH_SHORT).show();
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

    public void refreshTripMap(View view) {
        ArrayList<Waypoint> waypoints =  AppData.selectedTrip.getWaypoints();
        if (waypoints != null)
            Toast.makeText(this,"Number of waypoints: " + waypoints.size(),Toast.LENGTH_SHORT).show();
    }
}
