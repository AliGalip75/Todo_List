package com.example.todo_list.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
    }

    // Canlı veriyi UI'a gönder
    public LiveData<String> getText() {
        return mText;
    }
}