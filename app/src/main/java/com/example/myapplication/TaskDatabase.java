package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase instance;
    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(3);

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task-db")
                    .fallbackToDestructiveMigration() // Add this line for destructive migrations
                    .build();
        }
        return instance;
    }

    public abstract TaskDao taskDao();

}



