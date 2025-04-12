package com.example.todo_list.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.example.todo_list.data.Task;
import com.example.todo_list.data.TaskRepository;

public class CalendarViewModel extends AndroidViewModel {


    private final TaskRepository repository;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void insert(Task task) {
        repository.insert(task);
    }
}