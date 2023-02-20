package com.example.todos.core.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todos.core.database.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category

    @Insert
    suspend fun createCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}