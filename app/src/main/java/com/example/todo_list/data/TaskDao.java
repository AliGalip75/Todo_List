package com.example.todo_list.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// sql yazmadan veritabanı işlemlerini buradan yapıyoruz(bazı işlemler için sql yazmak gerekebiliyor)
@Dao
public interface TaskDao {
    @Delete
    void delete(Task task);

    @Insert
    void insert(Task task);

    @Query("SELECT * FROM task_table ORDER BY date DESC")
    LiveData<List<Task>> getAllTasks();

    @Update
    void update(Task task);
}
