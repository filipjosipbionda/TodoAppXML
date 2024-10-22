package com.bingugi.todoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bingugi.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTodo(todo: Todo)

        @Query("DELETE FROM todo WHERE date < :currentDate")
        suspend fun deleteExpiredTasks(currentDate: String)

        @Delete
        suspend fun deleteTask(todo: Todo)

        @Query("SELECT * FROM todo")
        fun getTodosStream():Flow<List<Todo>>

}