package com.example.todos.core.database.daos

import androidx.room.*
import com.example.todos.core.database.entities.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Int): Todo

    @Query("SELECT * FROM todo WHERE is_done = 0")
    fun getActiveTodos(): Flow<List<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}
