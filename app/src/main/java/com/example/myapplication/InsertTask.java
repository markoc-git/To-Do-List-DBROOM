package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class InsertTask extends AppCompatActivity {

    private EditText taskNameEditText;
    private EditText taskDescEditText;
    private TextView selectedDateTextView;

    private MainActivityViewModel taskViewModel;

    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_task);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        taskDescEditText = findViewById(R.id.taskDescText);
        Button selectDateButton = findViewById(R.id.selectDateButton);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        taskViewModel = new MainActivityViewModel(getApplication());

        selectDateButton.setOnClickListener(view -> showDatePickerDialog());

        saveTaskButton.setOnClickListener(view -> saveTask());

        cancelButton.setOnClickListener(view -> finish());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    selectedDateTextView.setText(selectedDate);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void saveTask() {
        String title = taskNameEditText.getText().toString().trim();
        String desc = taskDescEditText.getText().toString().trim();
        String date = selectedDate;

        Task task = new Task(title, desc, date);

        taskViewModel.insertTask(task);

        // Clear input fields
        taskNameEditText.getText().clear();
        taskDescEditText.getText().clear();
        selectedDateTextView.setText("");

        Toast.makeText(this, "Zadatak je sačuvan", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivityViewModel.class));
    }
}
