package com.example.todo_list.ui.profile;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<Integer> completedCount = new MutableLiveData<>();

    public ProfileViewModel() {
        this.completedCount = getCompletedCount();
    }

    public MutableLiveData<Integer> getCompletedCount() {
        return completedCount;
    }

    public void loadCompletedCount(Context context) {
        if (ProfileHelper.shouldReset(context)) {
            ProfileHelper.resetCompletedCount(context);
        }
        completedCount.setValue(ProfileHelper.getCompletedCount(context));
    }

    public void increment(Context context) {
        ProfileHelper.incrementCompletedCount(context);
        loadCompletedCount(context);
    }

    public void decrement(Context context) {
        ProfileHelper.decrementCompletedCount(context);
        loadCompletedCount(context);
    }

}