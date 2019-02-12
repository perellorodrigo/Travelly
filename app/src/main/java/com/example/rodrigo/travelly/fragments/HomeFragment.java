package com.example.rodrigo.travelly.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.activities.TripMainActivity;
import com.example.rodrigo.travelly.adapters.TripAdapter;
import com.example.rodrigo.travelly.models.Trip;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ListView list;
    TripAdapter adapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        // Link list layout element
        list = view.findViewById(R.id.listView);
        //Set trip adapter
        adapter = new TripAdapter(getContext(),AppData.loggedUserTrips);
        list.setAdapter(adapter);
        //

        //Set list click listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = adapter.getItem(position);
                Intent intent = new Intent(view.getContext(), TripMainActivity.class);
                AppData.selectedTrip = trip;
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter = new TripAdapter(getContext(),AppData.loggedUserTrips);
    }
}
