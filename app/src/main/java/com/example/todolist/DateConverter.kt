package com.example.todolist

import androidx.room.TypeConverter
import java.util.Date
import java.text.SimpleDateFormat

class DateConverter {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time
    }
}