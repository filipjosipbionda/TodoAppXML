package com.bingugi.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bingugi.todoapp.data.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
