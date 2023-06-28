package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTitleEditText;
    private EditText editDescriptionEditText;
    private Button updateButton;

    private TaskDao taskDao;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTitleEditText = findViewById(R.id.editTitleEditText);
        editDescriptionEditText = findViewById(R.id.editDescriptionEditText);
        updateButton = findViewById(R.id.updateButton);

        // Dobijanje ID zadatka iz Intent-a
        long taskId = getIntent().getLongExtra("taskId", -1);
        if (taskId == -1) {
            // ID zadatka nije validan, zatvaramo aktivnost
            finish();
            return;
        }

        // Inicijalizacija TaskDao
        TaskDatabase taskDatabase = TaskDatabase.getInstance(getApplicationContext());
        taskDao = taskDatabase.taskDao();

        // Dobijanje zadatka iz baze podataka na osnovu ID-ja
        task = taskDao.getTaskById(taskId);
        if (task == null) {
            // Zadatak nije pronađen, zatvaramo aktivnost
            finish();
            return;
        }

        // Postavljanje trenutnih vrednosti zadatka u EditText polja
        editTitleEditText.setText(task.getTitle());
        editDescriptionEditText.setText(task.getDescription());

        // Slušač događaja za dugme "Update"
        updateButton.setOnClickListener(v -> {
            // Dobijanje ažuriranih vrednosti iz EditText polja
            String updatedTitle = editTitleEditText.getText().toString().trim();
            String updatedDescription = editDescriptionEditText.getText().toString().trim();

            // Ažuriranje zadatka
            task.setTitle(updatedTitle);
            task.setDescription(updatedDescription);
            updateTask(task);

            // Završetak aktivnosti
            finish();
        });
    }

    private void updateTask(Task task) {
        new Thread(() -> taskDao.update(task)).start();
    }
}
