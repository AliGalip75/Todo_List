package com.example.todo_list.ui.calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_list.data.Task;
import com.example.todo_list.data.TaskRepository;

public class CalendarViewModel extends AndroidViewModel {

    /*
    private final MutableLiveData<String> mText;
    private TaskRepository repository;
    public CalendarViewModel(Application application) {
        mText = new MutableLiveData<>();
        //mText.setValue("This is calendar fragment");
        repository = new TaskRepository(application);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void insert(Task task) {
        repository.insert(task);
    }

     */

    private TaskRepository repository;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void insert(Task task) {
        repository.insert(task);
    }
}