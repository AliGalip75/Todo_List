package com.example.todo_list.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public void insert(Task task) {
        TaskDatabase.databaseWriteExecutor.execute( () -> taskDao.insert(task));
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public void deleteTask(Task task){
        TaskDatabase.databaseWriteExecutor.execute( () -> taskDao.delete(task));
    }

    public void update(Task task) {
        TaskDatabase.databaseWriteExecutor.execute( () -> taskDao.update(task));
    }

    public LiveData<List<Task>> getSortedTasks() {
        return taskDao.getTasksSortedByDateAndPriority();
    }

}


