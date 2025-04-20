package com.example.todo_list.ui.profile;

import android.content.Context;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class ProfileHelper {

    private static final String PREF_NAME = "stats_pref";
    private static final String KEY_COMPLETED = "completed_count";
    private static final String KEY_DAILY_COMPLETED = "daily_completed_"; // Ön ek olarak kullanılacak
    private static final String KEY_LAST_CHECKED_DATE = "last_checked_date";

    // Belirli bir gün için görev sayısını set et
    public static void setCompletedCountForDay(Context context, int dayOfWeek, int count) {
        String key = KEY_DAILY_COMPLETED + dayOfWeek;
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, count)
                .apply();
    }

    // Belirli bir gün için görev sayısını getir
    public static int getCompletedCountForDay(Context context, int dayOfWeek) {
        String key = KEY_DAILY_COMPLETED + dayOfWeek;
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

    // Belirli bir gün için tamamlanan görev sayısını bir artır. (toplam görev sayısı da artacak)
    public static void incrementCompletedCountForDay(Context context, int dayOfWeek) {
        int current = getCompletedCountForDay(context, dayOfWeek); // Şu an o gün için kaç görev var?
        setCompletedCountForDay(context, dayOfWeek, current + 1); // o gün için görev sayısını bir artırarak set et
        incrementCompletedCount(context); // Toplam sayıyı da bir artır
    }

    // Haftalık verileri sıfırla (Pazar gece yarısı otomatik)
    public static void resetIfNeeded(Context context) {
        LocalDate today = LocalDate.now(); // Bu günün tarihi
        LocalDate lastChecked = getLastCheckedDate(context); // En son kontrol edilen tarih

        // Eğer bugün Pazar ve son kontrol tarihi Pazartesi-Perşembe arasındaysa sıfırla
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY &&
                (lastChecked == null || lastChecked.isBefore(today))) {
            resetWeeklyData(context);
        }
        setLastCheckedDate(context, today); // En son kontrol tarihini bugün olarak set et
    }

    // Haftalık verileri sıfırla
    public static void resetWeeklyData(Context context) {
        for (int i = 0; i < 7; i++) { // Her gün için count değerlerini sıfırla
            setCompletedCountForDay(context, i, 0);
        }
        setCompletedCount(context, 0); // Toplam count'u sıfırla
    }

    // Toplam tamamlanan görev sayısını getir
    public static int getCompletedCount(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_COMPLETED, 0);
    }

    // Toplam tamamlanan görev sayısını set et
    public static void setCompletedCount(Context context, int count) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_COMPLETED, count)
                .apply();
    }

    // Toplam tamamlanan görev sayısını artır
    public static void incrementCompletedCount(Context context) {
        int current = getCompletedCount(context);
        setCompletedCount(context, current + 1);
    }

    // Son kontrol tarihini set et
    private static void setLastCheckedDate(Context context, LocalDate date) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LAST_CHECKED_DATE, date.toString())
                .apply();
    }

    // Son kontrol tarihini getir
    private static LocalDate getLastCheckedDate(Context context) {
        String dateStr = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LAST_CHECKED_DATE, null);
        return dateStr != null ? LocalDate.parse(dateStr) : null;
    }
}