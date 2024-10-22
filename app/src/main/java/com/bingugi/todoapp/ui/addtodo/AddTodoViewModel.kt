package com.bingugi.todoapp.ui.addtodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingugi.todoapp.data.model.Todo
import com.bingugi.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTodoViewModel(private val repository: TodoRepository) : ViewModel() {
    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }
}
