package com.example.todo_list.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase INSTANCE;

    // insert, delete, getAllTasks gibi DAO fonksiyonlarına buradan erişilir.
    public abstract TaskDao taskDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static synchronized TaskDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskDatabase.class, "task_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

