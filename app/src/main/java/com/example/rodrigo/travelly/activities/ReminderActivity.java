package com.example.rodrigo.travelly.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.adapters.ReminderAdapter;
import com.example.rodrigo.travelly.models.Reminder;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    ListView reminderListView;
    EditText reminderTitleInput;
    EditText reminderDescription;
    DatabaseHelper databaseHelper;
    ArrayList<Reminder> reminders;
    ReminderAdapter adapter;
    Context context;
    Reminder selectedReminder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        setTitle("Reminders");
        // Link layout elements
        reminderListView = findViewById(R.id.reminderListView);
        reminderTitleInput = findViewById(R.id.reminderTitleInput);
        reminderDescription = findViewById(R.id.reminderDescriptionInput);
        //---

        //Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        //Set context
        context = this;

        // Get reminders from database
        reminders = databaseHelper.getRemindersByTripID(AppData.selectedTrip.getId());
        adapter = new ReminderAdapter(this,reminders);

        //Link adapter to list view
        reminderListView.setAdapter(adapter);
        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedReminder = adapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to delete this reminder?")
                        .setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();
                return false;
            }
        });

    }

    //Confirm delete dialog
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(context,"To be implemented in next versions",Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };


    public void AddNewReminder(View view) {
        //Add new reminder to database
        Reminder reminder = databaseHelper.addReminder(new Reminder(reminderTitleInput.getText().toString(),
                                                reminderDescription.getText().toString()),
                                    AppData.selectedTrip.getId());
        //If reminder was added, add it to the array list to display in the list view
        if(reminder != null) {
            reminders.add(reminder);
            adapter.notifyDataSetChanged();
        }
    }
}
