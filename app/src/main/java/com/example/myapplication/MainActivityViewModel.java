package com.example.myapplication;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> tasksLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        TaskDatabase taskDatabase = TaskDatabase.getInstance(application);
        taskDao = taskDatabase.taskDao();
        tasksLiveData = taskDao.getAllTasks();


    }

    public void loadTasks() {
        // Nema potrebe za posebnom metodom za učitavanje jer LiveData automatski osluškuje promjene u bazi podataka
    }

    public void insertTask(Task task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public void updateTask(Task task) {
        new Thread(() -> taskDao.update(task)).start();
    }

    public void deleteTask(Task task) {
        new Thread(() -> taskDao.delete(task)).start();
    }

    public LiveData<List<Task>> getSortedTasksLiveData() {
        return taskDao.getAllTasksOrderByTime();
    }

}
