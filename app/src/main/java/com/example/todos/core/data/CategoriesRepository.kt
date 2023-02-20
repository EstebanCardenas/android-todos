package com.example.todos.core.data

import com.example.todos.core.database.TodoDatabase
import com.example.todos.core.database.entities.Category
import kotlinx.coroutines.flow.Flow

class CategoriesRepository(private val database: TodoDatabase) {
    val categories: Flow<List<Category>> = database.categoryDao().getCategories()

    suspend fun createCategory(category: Category) {
        database.categoryDao().createCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        database.categoryDao().deleteCategory(category)
        database.categoryWithTodosDao().deleteAllTodosForCategory(category.id)
    }

    suspend fun getCategoryById(categoryId: Int): Category {
        return database.categoryDao().getCategoryById(categoryId)
    }

    fun getNumberOfTasksForCategory(categoryId: Int): Flow<Int> {
        return database.categoryWithTodosDao().getNumberOfTasksForCategory(categoryId)
    }
}
