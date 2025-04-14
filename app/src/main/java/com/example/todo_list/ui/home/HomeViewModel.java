package com.example.todo_list.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_list.data.Task;
import com.example.todo_list.data.TaskRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {


    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }

    public void updateTask(Task task) {
        repository.update(task);  // Repository üzerinden güncelleme işlemi
    }

}