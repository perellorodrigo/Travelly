package com.example.rodrigo.travelly.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.activities.TripMainActivity;
import com.example.rodrigo.travelly.adapters.TripAdapter;
import com.example.rodrigo.travelly.models.Trip;
import com.example.rodrigo.travelly.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ListView list;
    TripAdapter adapter;
    ArrayList<Trip> trips;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);


        trips = AppData.loggedUserTrips;
        list = (ListView) view.findViewById(R.id.listView);

        adapter = new TripAdapter(inflater.getContext(),trips);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = adapter.getItem(position);
                Toast.makeText(view.getContext(),"Clicked: " + trip.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), TripMainActivity.class);
                intent.putExtra("selectedTrip",trip);
                startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        trips = AppData.loggedUserTrips;
        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(),"Resume called",Toast.LENGTH_SHORT).show();
    }
}
