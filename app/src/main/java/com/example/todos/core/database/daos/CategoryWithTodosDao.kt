package com.example.todos.core.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.todos.core.database.relations.CategoryWithTodos
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryWithTodosDao {
    @Transaction
    @Query("SELECT * FROM category WHERE id = :categoryId")
    fun getTodosFromCategoryId(categoryId: Int): Flow<CategoryWithTodos>

    @Query("SELECT count(*) FROM todo WHERE category_id = :categoryId")
    fun getNumberOfTasksForCategory(categoryId: Int): Flow<Int>

    @Query("DELETE FROM todo WHERE category_id = :categoryId")
    suspend fun deleteAllTodosForCategory(categoryId: Int)
}