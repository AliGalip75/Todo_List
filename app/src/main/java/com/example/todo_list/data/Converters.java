package com.example.todo_list.data;

import androidx.room.TypeConverter;

// Room doğrudan enum versini işleyemez, convert etmemiz lazım
public class Converters {
    @TypeConverter
    public static String fromPriority(Priority priority) {
        return priority == null ? Priority.Medium.name() : priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String value) {
        try {
            return Priority.valueOf(value);
        } catch (Exception e) {
            return Priority.Medium;
        }
    }
}

