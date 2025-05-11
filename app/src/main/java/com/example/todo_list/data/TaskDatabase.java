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

@Database(entities = {Task.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskDatabase extends RoomDatabase {
    private static volatile TaskDatabase INSTANCE;

    public abstract TaskDao taskDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(2);

    /** 2 → 3 sürüm geçişi: priority ve category sütunlarını ekler */
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            // priority ve category sütunlarını nullable, default tanımsız olarak ekle
            db.execSQL("ALTER TABLE task_table ADD COLUMN priority TEXT");
            db.execSQL("ALTER TABLE task_table ADD COLUMN category TEXT");
        }
    };


    public static synchronized TaskDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TaskDatabase.class,
                                    "task_database"
                            )
                            // fallbackToDestructiveMigration();
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
