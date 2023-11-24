package com.example.todolist

interface OnItemClickListener {

    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)
}