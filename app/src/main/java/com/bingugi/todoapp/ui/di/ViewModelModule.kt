package com.bingugi.todoapp.ui.di

import com.bingugi.todoapp.ui.addtodo.AddTodoViewModel
import com.bingugi.todoapp.ui.todolist.TodoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule= module {
    viewModel { TodoListViewModel(get()) }
    viewModel { AddTodoViewModel(get()) }
}