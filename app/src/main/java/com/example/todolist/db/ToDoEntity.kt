package com.example.todolist.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.io.Serializable


@Entity
data class ToDoEntity (
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name="title") val title : String,
    @ColumnInfo(name="importance") val importance : Int,
    @ColumnInfo(name="date") val date : Date? = null,
    @ColumnInfo(name="isVisible") var isVisible : Boolean = false,
    @ColumnInfo(name="isCheck") var isCheck : Boolean = false
) : Serializable