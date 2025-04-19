package com.example.todo_list.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static volatile TaskDatabase INSTANCE;

    // insert, delete, getAllTasks gibi DAO fonksiyonlarına buradan erişilir.
    public abstract TaskDao taskDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(2); // 2 thread yeterli

    public static synchronized TaskDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                             TaskDatabase.class, "task_database").fallbackToDestructiveMigration()  // versiyon artınca veritabanını sıfırlar(geliştirme aşaması)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

