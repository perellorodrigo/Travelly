package com.example.rodrigo.travelly.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Waypoint;

import java.text.ParseException;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddWaypointFragment extends DialogFragment{

    private EditText waypointDescription;
    private EditText waypointDate;
    private Button doneButton;
    final Calendar myCalendar = Calendar.getInstance();




    public AddWaypointFragment() {
        // Required empty public constructor
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Link layout elements
        waypointDescription = view.findViewById(R.id.waypointDescription);
        waypointDescription.requestFocus();
        waypointDate = view.findViewById(R.id.waypointDate);
        doneButton = view.findViewById(R.id.doneButton);


        //Set click listener for waypoint expected date
        waypointDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWaypointDialogListener listener = (AddWaypointDialogListener) getActivity();
                Waypoint waypoint = null;
                try {
                    waypoint = new Waypoint(waypointDescription.getText().toString(),AppData.dateFormat.parse(waypointDate.getText().toString()),null);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Date parse Exception! ex:" + e, Toast.LENGTH_SHORT).show();
                }

                listener.onFinishEditDialog(waypoint);
                dismiss();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            waypointDate.setText(AppData.dateFormat.format(myCalendar.getTime()));
            doneButton.setEnabled(true);
        }

    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_waypoint,container);
    }

    public interface AddWaypointDialogListener {
        void onFinishEditDialog(Waypoint wp);
    }

    public static AddWaypointFragment newInstance() {
        AddWaypointFragment frag = new AddWaypointFragment();
        return frag;
    }


}
