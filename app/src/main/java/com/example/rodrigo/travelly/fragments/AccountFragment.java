package com.example.rodrigo.travelly.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.activities.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    Button logoutButton;
    SharedPreferences sp;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_account, container, false);
        sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("logged",false).apply();
                Intent loginActivityIntent = new Intent(getActivity(),LoginActivity.class);
                String userEmail = sp.getString("activeUserEmail",null);
                loginActivityIntent.putExtra("email",userEmail);
                startActivity(loginActivityIntent);
            }
        });


        return view;
    }

}
