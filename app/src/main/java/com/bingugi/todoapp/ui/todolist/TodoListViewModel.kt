package com.bingugi.todoapp.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bingugi.todoapp.data.model.Todo
import com.bingugi.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TodoListViewModel(private val repository: TodoRepository) : ViewModel(){

    private var _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>>
        get() = _todos



    init {
        viewModelScope.launch {
            val date = LocalDate.now()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
            val formattedDate=dateTimeFormatter.format(date)

            withContext(this.coroutineContext) {
                repository.todos().collect {
                   _todos.postValue(it)
                }
            }

            withContext(this.coroutineContext) {
                repository.deleteExpiredTasks(formattedDate)
            }
        }
    }


    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

     fun createTodoList(): List<TodoListItem> {
        val todoList = mutableListOf<TodoListItem>()


        val groupedTodos = _todos.value?.groupBy { it.date }

        if(!groupedTodos.isNullOrEmpty()) {
            for ((date, tasks) in groupedTodos) {
                todoList.add(TodoListItem.Header(date))
                val sortedTasks =
                    tasks.sortedWith(compareBy<Todo> { it.priority }.thenBy { it.name })

                sortedTasks.forEach{
                    val todoListItemTodo= TodoListItem
                        .Todo(
                        name = it.name,
                        date = it.date,
                        description = it.description,
                        priority = it.priority,
                        id=it.id,
                    )
                    todoList.add(todoListItemTodo)
                }
            }
        }
        return todoList
    }
}