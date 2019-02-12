package com.example.rodrigo.travelly.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rodrigo.travelly.models.Reminder;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public ReminderAdapter(@NonNull Context context, ArrayList<Reminder> reminders) {
        super(context, android.R.layout.simple_list_item_2,reminders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reminder reminder = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // Inflate the view -> using default list layout
        View view = inflater.inflate(android.R.layout.simple_list_item_2,parent,false);

        //Link layout elements
        TextView title = view.findViewById(android.R.id.text1);
        TextView description = view.findViewById(android.R.id.text2);
        //---

        //Set reminder details
        title.setText(reminder.getTitle());
        description.setText(reminder.getDescription());

        return view;

    }
}
