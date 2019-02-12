package com.example.rodrigo.travelly.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rodrigo.travelly.R;
import com.example.rodrigo.travelly.models.Expense;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    public ExpenseAdapter(@NonNull Context context, ArrayList<Expense> expenses) {
        super(context, R.layout.expense_row_layout,expenses);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Expense expense = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // Inflate the view
        View view = inflater.inflate(R.layout.expense_row_layout,parent,false);

        //Link layout elements
        TextView expenseDate = view.findViewById(R.id.expenseDate);
        TextView expenseDescription = view.findViewById(R.id.expenseDescription);
        TextView expenseAmount = view.findViewById(R.id.expenseAmount);
        //------

        //Set expense details
        expenseDate.setText(android.text.format.DateFormat.format("dd/MM",expense.getDate()));
        expenseDescription.setText(expense.getDescription());
        expenseAmount.setText(String.format("$%.2f",expense.getAmount()));

        return view;
    }
}
