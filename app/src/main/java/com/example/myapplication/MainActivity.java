package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        taskAdapter = new TaskAdapter(mainActivityViewModel);
        recyclerView.setAdapter(taskAdapter);

        mainActivityViewModel.getSortedTasksLiveData().observe(this, tasks -> taskAdapter.setTasks(tasks));

        loadTasks();
    }

    private void loadTasks() {
        new Thread(() -> mainActivityViewModel.loadTasks()).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(this, InsertTask.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
