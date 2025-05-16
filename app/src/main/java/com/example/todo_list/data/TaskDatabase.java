package com.example.todo_list.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskDatabase extends RoomDatabase {
    private static volatile TaskDatabase INSTANCE;

    public abstract TaskDao taskDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(2);

    public static synchronized TaskDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TaskDatabase.class,
                                    "task_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
