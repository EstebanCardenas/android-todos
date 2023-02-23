package com.example.todos.core.database.daos

import androidx.room.*
import com.example.todos.core.database.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY is_favorite DESC")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category

    @Update
    suspend fun updateCategory(category: Category)

    @Insert
    suspend fun createCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}