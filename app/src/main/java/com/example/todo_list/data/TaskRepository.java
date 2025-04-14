package com.example.todo_list.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private final TaskDao taskDao;
    private ExecutorService executorService;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        executorService = Executors.newSingleThreadExecutor();
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

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));  // Arka planda çalıştır
    }

}


