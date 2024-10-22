package com.bingugi.todoapp.data.repository

import com.bingugi.todoapp.data.model.Todo
import com.bingugi.todoapp.data.database.TodoDao


class TodoRepository(private val todoDao: TodoDao) {

    suspend fun insertTodo(todo: Todo){
        todoDao.insertTodo(todo)
    }

    suspend fun deleteExpiredTasks(date: String){
        todoDao.deleteExpiredTasks(date)
    }

    suspend fun deleteTodo(todo: Todo){
        todoDao.deleteTask(todo)
    }

    fun todos()=todoDao.getTodosStream()

}