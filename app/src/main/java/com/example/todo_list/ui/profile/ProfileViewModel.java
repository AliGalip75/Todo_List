package com.example.todo_list.ui.profile;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> completedCount = new MutableLiveData<>();
    private final MutableLiveData<Integer[]> weeklyData = new MutableLiveData<>();

    public ProfileViewModel(Application application) {
        super(application);
        loadData();
    }

    public LiveData<Integer> getCompletedCount() {
        return completedCount;
    }

    public LiveData<Integer[]> getWeeklyData() {
        return weeklyData;
    }

    public void loadData() {
        Context context = getApplication();

        // Otomatik reset kontrolü
        ProfileHelper.resetIfNeeded(context);

        // Toplam tamamlanan görevleri yükle
        completedCount.setValue(ProfileHelper.getCompletedCount(context));

        // Haftalık verileri yükle
        Integer[] weeklyCounts = new Integer[7];
        for (int i = 0; i < 7; i++) {
            weeklyCounts[i] = ProfileHelper.getCompletedCountForDay(context, i);
        }
        weeklyData.setValue(weeklyCounts);
    }

    public void incrementDay(Context context, int dayOfWeek) {
        ProfileHelper.incrementCompletedCountForDay(context, dayOfWeek);
        loadData();
    }
}