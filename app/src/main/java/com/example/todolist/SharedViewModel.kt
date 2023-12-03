package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.db.ToDoEntity
class SharedViewModel : ViewModel() {
    private val _deletedItems = MutableLiveData<List<ToDoEntity>>()
    val deletedItems: LiveData<List<ToDoEntity>> get() = _deletedItems

    fun setDeletedItems(items: List<ToDoEntity>) {
        _deletedItems.value = items
    }
}

