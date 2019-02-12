package com.example.rodrigo.travelly.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.MapsHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.adapters.ExpenseAdapter;
import com.example.rodrigo.travelly.models.Expense;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class BudgetActivity extends AppCompatActivity {

    TextView totalExpensesTextView, addedExpensesTextView, estimatedFuelExpensesTextView;
    EditText expenseDescriptionInput, expenseAmountInput, expenseDateInput;
    Button addNewExpenseButton;
    ListView expensesList;
    ExpenseAdapter expenseAdapter;
    Expense selectedExpense;
    ArrayList<Expense> expenses;
    float totalExpenses = 0, addedExpenses = 0, estimatedFuelExpenses = 0;
    final Calendar myCalendar = Calendar.getInstance();
    private DatabaseHelper databaseHelper;
    private Context context = this;

    // Average fuel cost in Australia
    // TODO In next version, get real time fuel cost or user input values
    private static final float FUEL_COST = 1.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Budget");
        setContentView(R.layout.activity_budget);
        // Link layout elements
        totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        addedExpensesTextView = findViewById(R.id.addedExpensesTextView);
        estimatedFuelExpensesTextView = findViewById(R.id.estimatedFuelExpensesTextView);
        addNewExpenseButton = findViewById(R.id.addExpenseButton);
        expenseDescriptionInput = findViewById(R.id.expenseDescriptionInput);
        expenseAmountInput = findViewById(R.id.expenseAmountInput);
        expenseDateInput = findViewById(R.id.expenseDateInput);
        expensesList = findViewById(R.id.expensesListView);
        //

        //Set click listener for Date Picker
        expenseDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Create Database context
        databaseHelper = new DatabaseHelper(this);

        // Calculate user added expenses and estimated fuel expenses
        computeAddedExpenses();
        estimateFuelExpense();

        // Calculate total expenses
        totalExpenses = addedExpenses + estimatedFuelExpenses;
        // Set total expenses text
        totalExpensesTextView.setText("Total Expenses: $" + String.format("%.2f",totalExpenses));


        // Sort expenses by date
        // The sorting method is defined in Expense Class
        Collections.sort(expenses);

        //Set the adapter for list find
        expenseAdapter = new ExpenseAdapter(this, expenses);
        expensesList.setAdapter(expenseAdapter);
        expensesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO next versions: implement delete expense
                selectedExpense = expenseAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Would you like to delete this expense?")
                        .setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();
                return false;
            }
        });

    }

    //Date picker Dialog
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // Set the expense date
            expenseDateInput.setText(AppData.dateFormat.format(myCalendar.getTime()));
        }
    };

    //Confirm Dialog to delete expense
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

    //Add new Expense Function
    public void AddNewExpense(View view) {
        //Get values:
        String expenseDescription = expenseDescriptionInput.getText().toString();
        Float expenseAmount = Float.parseFloat(expenseAmountInput.getText().toString());
        Date expenseDate;
        //---------------
        try {
            expenseDate = AppData.dateFormat.parse(expenseDateInput.getText().toString());
            Expense expense = databaseHelper.addExpense(new Expense(expenseDescription,expenseAmount, expenseDate),AppData.selectedTrip.getId());

            if(expense != null)
            {
                //Add expense to expenses array
                expenses.add(expense);
                //Sum expense to total values
                totalExpenses += expense.getAmount();
                addedExpenses += expense.getAmount();
                //Update text views
                totalExpensesTextView.setText("Total Expenses: $" + String.format("%.2f",totalExpenses));
                addedExpensesTextView.setText("Added Expenses: $" + String.format("%.2f",addedExpenses));
                //Sort expenses according to date
                Collections.sort(expenses);
                //Notify DataSet change
                expenseAdapter.notifyDataSetChanged();
                Toast.makeText(this,"Added a new Expense",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"Couldn't add new Expense", Toast.LENGTH_SHORT).show(); // Database problem
        } catch (ParseException e) {
            //Throws exception when there is error in parsing the Date to string
            e.printStackTrace();
            Toast.makeText(this,"Couldn't add new Expense", Toast.LENGTH_SHORT).show(); // Parse exception problem
        }
    }

    private void estimateFuelExpense(){
        // Estimate fuel expense based on trip distance and Fuel cost
        // Using a constant for Fuel Cost, implementation of variable fuel cost to be done in next versions

        // Get the trip distance
        long tripDistanceInMeters = MapsHelper.tripDistanceInMeters;
        // Get vehicle consumption
        float vehicleConsumption = AppData.selectedTrip.getVehicle().getFuelConsumption();

        // Calculate values
        float fuelConsumedInLiters = tripDistanceInMeters / (vehicleConsumption * 1000f);
        estimatedFuelExpenses = fuelConsumedInLiters * FUEL_COST;
        //Set text with two decimal precision
        estimatedFuelExpensesTextView.setText("Fuel Estimate: $" + String.format("%.2f",estimatedFuelExpenses));
    }

    private void computeAddedExpenses(){
        // Get the expenses from the database
        expenses = databaseHelper.getExpensesByTripID(AppData.selectedTrip.getId());
        //Add all expenses amount
        addedExpenses = 0;
        for(Expense ex : expenses){
            addedExpenses += ex.getAmount();
        }
        //Set text for expenses with two decimal precision
        addedExpensesTextView.setText("Added Expenses: $" + String.format("%.2f",addedExpenses));
    }
}
