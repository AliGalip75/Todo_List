package com.example.todo_list.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
@Dao
public interface TaskDao { // sql yazmadan veritabanı işlemlerini buradan yapıyoruz(bazı işlemler için sql yazmak gerekebilir)
    @Delete
    void delete(Task task);

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM task_table ORDER BY date ASC")
    LiveData<List<Task>> getAllTasks();

    @Update
    void update(Task task);

    @Query("""
    SELECT * FROM task_table 
    ORDER BY 
        date ASC,
        CASE priority
            WHEN 'HIGH' THEN 1
            WHEN 'MEDIUM' THEN 2
            WHEN 'LOW' THEN 3
            ELSE 4
        END
    """)
    LiveData<List<Task>> getTasksSortedByDateAndPriority();

    @Query("SELECT * FROM task_table WHERE date = :date ORDER BY id DESC")
    LiveData<List<Task>> getTasksByDate(String date);
}
