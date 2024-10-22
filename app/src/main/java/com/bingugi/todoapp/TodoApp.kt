package com.bingugi.todoapp

import android.app.Application
import com.bingugi.todoapp.data.di.dataModule
import com.bingugi.todoapp.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TodoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin here
        startKoin {
            androidContext(this@TodoApp)
            modules(dataModule, viewModelModule)
        }
    }
}
