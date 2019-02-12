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
import android.widget.TextView;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.activities.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    Button logoutButton;
    SharedPreferences sp;
    TextView userMessage, userEmail;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_account, container, false);
        userMessage = view.findViewById(R.id.userMessage);
        userEmail =  view.findViewById(R.id.userEmail);

        sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        userMessage.setText("Hello " + AppData.loggedUser.getName());
        userEmail.setText("Your email: " + AppData.loggedUser.getEmail());


        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("logged",false).apply();
                sp.edit().putInt("activeUserID",-1).apply();
                Intent loginActivityIntent = new Intent(getActivity(),LoginActivity.class);
                String userEmail;
                if(AppData.loggedUser != null) {
                    userEmail = AppData.loggedUser.getEmail();
                    loginActivityIntent.putExtra("email", userEmail);
                }
                startActivity(loginActivityIntent);
                getActivity().finish();
            }
        });


        return view;
    }

}
