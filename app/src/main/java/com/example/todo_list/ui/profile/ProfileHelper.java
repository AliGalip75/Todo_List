package com.example.todo_list.ui.profile;

import android.content.Context;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProfileHelper { // Bu sınıf Profile sayfasında sharedPreference kullanımı için

    private static final String PREF_NAME = "stats_pref";
    private static final String KEY_COMPLETED = "completed_count";
    private static final String KEY_LAST_RESET = "last_reset_date";

    public static int getCompletedCount(Context context) { // Count değerini al
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_COMPLETED, 0);
    }

    public static void setCompletedCount(Context context, int count) { // Count değerini set et
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_COMPLETED, count)
                .apply();
    }

    public static void incrementCompletedCount(Context context) { // Count değerini bir artır
        int current = getCompletedCount(context);
        setCompletedCount(context, current + 1);
    }

    public static void decrementCompletedCount(Context context) { // Count değerini bir azalt
        int current = getCompletedCount(context);
        if (current > 0) {
            setCompletedCount(context, current - 1);
        }
    }

    public static String getLastResetDate(Context context) { // En sonki reset tarihini getir
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LAST_RESET, "");
    }

    public static void setLastResetDate(Context context, String date) { // En sonki reset tarihini tut
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LAST_RESET, date)
                .apply();
    }

    public static boolean shouldReset(Context context) { // Count sıfırlama zamanı geldi mi?
        String lastDateStr = getLastResetDate(context);
        if (lastDateStr.isEmpty()) return true;

        LocalDate lastDate = LocalDate.parse(lastDateStr);
        return ChronoUnit.DAYS.between(lastDate, LocalDate.now()) >= 7;
    }

    public static void resetCompletedCount(Context context) { // Haftada bir defa count sıfırla
        setCompletedCount(context, 0);
        setLastResetDate(context, LocalDate.now().toString());
    }
}

