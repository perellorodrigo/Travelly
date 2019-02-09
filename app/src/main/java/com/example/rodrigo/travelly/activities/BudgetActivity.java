package com.example.rodrigo.travelly.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodrigo.travelly.AppData;
import com.example.rodrigo.travelly.DatabaseHelper;
import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.adapters.ExpenseAdapter;
import com.example.rodrigo.travelly.models.Expense;
import com.example.rodrigo.travelly.models.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BudgetActivity extends AppCompatActivity {

    TextView totalExpensesTextView;
    EditText expenseDescriptionInput, expenseAmountInput, expenseDateInput;
    Button addNewExpenseButton;
    ListView list;
    ExpenseAdapter expenseAdapter;
    private DatabaseHelper databaseHelper;
    ArrayList<Expense> expenses;
    float totalExpenses = 0;
    final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        totalExpensesTextView = findViewById(R.id.totalExpensesTextView);
        addNewExpenseButton = findViewById(R.id.addExpenseButton);
        expenseDescriptionInput = findViewById(R.id.expenseDescriptionInput);
        expenseAmountInput = findViewById(R.id.expenseAmountInput);
        expenseDateInput = findViewById(R.id.expenseDateInput);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                expenseDateInput.setText(dateFormat.format(myCalendar.getTime()));
            }

        };
        expenseAdapter = null;

        expenseDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        databaseHelper = new DatabaseHelper(this);



        expenses = databaseHelper.getExpensesByTripID(AppData.selectedTrip.getId());
        if (expenses == null)
            expenses = new ArrayList<>();

        totalExpenses = 0;
        for(Expense ex : expenses){
            totalExpenses += ex.getAmount();
        }
        totalExpensesTextView.setText("Total: $" + Float.toString(totalExpenses));

        expenseAdapter = new ExpenseAdapter(this,expenses);

        list = findViewById(R.id.expensesListView);

        list.setAdapter(expenseAdapter);

    }

    public void AddNewExpense(View view) {
        String expenseDescription = expenseDescriptionInput.getText().toString();
        Float expenseAmount = Float.parseFloat(expenseAmountInput.getText().toString());
        Date expenseDate;
        try {
            expenseDate = dateFormat.parse(expenseDateInput.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            expenseDate = null;
        }

        Expense expense = databaseHelper.addExpense(new Expense(expenseDescription,expenseAmount, expenseDate),AppData.selectedTrip.getId());

        if(expense != null)
        {
            expenses.add(expense);

            totalExpenses += expense.getAmount();
            totalExpensesTextView.setText("Total: $" + Float.toString(totalExpenses));
            expenseAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Added a new Expense",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"Couldn't add new Expense", Toast.LENGTH_SHORT).show();
    }
}
