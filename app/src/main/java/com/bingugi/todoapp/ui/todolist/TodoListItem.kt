package com.bingugi.todoapp.ui.todolist

import com.bingugi.todoapp.data.model.Priority

sealed class TodoListItem {
    data class Header(var date:String): TodoListItem()

    data class Todo(
        val name:String,
        val date:String,
        val description:String,
        val priority: Priority,
        val id:Long
    ): TodoListItem()
}