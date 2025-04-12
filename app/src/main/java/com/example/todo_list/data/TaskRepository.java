package com.example.todo_list.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> {
            taskDao.insert(task);
        });
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public void deleteTask(Task task){
        TaskDatabase.databaseWriteExecutor.execute(() -> taskDao.delete(task));
    }

}


