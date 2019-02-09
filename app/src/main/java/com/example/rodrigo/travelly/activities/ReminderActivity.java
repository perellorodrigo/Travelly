package com.example.rodrigo.travelly.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.adapters.ReminderAdapter;
import com.example.rodrigo.travelly.models.Expense;
import com.example.rodrigo.travelly.models.Reminder;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    ListView reminderListView;
    EditText reminderTitleInput;
    EditText reminderDescription;
    DatabaseHelper databaseHelper;
    ArrayList<Reminder> reminders;
    ReminderAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        reminderListView = findViewById(R.id.reminderListView);
        reminderTitleInput = findViewById(R.id.reminderTitleInput);
        reminderDescription = findViewById(R.id.reminderDescriptionInput);
        databaseHelper = new DatabaseHelper(this);

        reminders = databaseHelper.getRemindersByTripID(AppData.selectedTrip.getId());
        if (reminders == null)
            reminders = new ArrayList<>();

        adapter = new ReminderAdapter(this,reminders);
        reminderListView.setAdapter(adapter);



        //expensesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);





    }

    public void AddNewReminder(View view) {
        Reminder reminder = databaseHelper.addReminder(new Reminder(reminderTitleInput.getText().toString(),
                                                reminderDescription.getText().toString()),
                                    AppData.selectedTrip.getId());
        if(reminder != null)
            adapter.add(reminder);
        adapter.notifyDataSetChanged();

    }
}
