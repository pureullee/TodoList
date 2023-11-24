package com.example.todolist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {
    @Query("SELECT * FROM ToDoEntity")
    fun getAll() : List<ToDoEntity>

    @Insert
    fun insertTodo(todo : ToDoEntity)

    @Delete
    fun deleteTodo(todo : ToDoEntity)

    @Update
    fun updateTodo(todo : ToDoEntity)
}