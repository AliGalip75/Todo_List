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

    /*
    private final MutableLiveData<String> mText;

    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment :))");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void changeText(String newText) {
        mText.setValue(newText);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

     */

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
}