package com.example.todo_list.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String date;
    private Boolean isDone;
    private Priority priority;
    private String category;

    public Task(String title, String date, Boolean isDone) {
        this.title = title;
        this.date = date;
        this.isDone = isDone;
        this.priority = Priority.MEDIUM;
        this.category = "General";
    }

    // Tam tüm alanları kapsayan constructor
    public Task(int id, String title, boolean isDone,
                Priority priority, String category) {
        this.id = id;
        this.title = title;
        this.isDone = isDone;
        this.priority = priority;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

