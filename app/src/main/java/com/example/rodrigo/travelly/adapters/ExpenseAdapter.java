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
        View view = inflater.inflate(R.layout.expense_row_layout,parent,false);

        TextView expenseDescription = (TextView) view.findViewById(R.id.expenseDescription);
        TextView expenseAmount = (TextView) view.findViewById(R.id.expenseAmount);

        expenseDescription.setText(expense.getDescription());
        expenseAmount.setText(Float.toString(expense.getAmount()));

        return view;
    }
}
