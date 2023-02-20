package com.example.todos.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todos.core.database.daos.CategoryDao
import com.example.todos.core.database.daos.CategoryWithTodosDao
import com.example.todos.core.database.daos.TodoDao
import com.example.todos.core.database.entities.Category
import com.example.todos.core.database.entities.Todo

@Database(entities = [Todo::class, Category::class], version = 5)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryWithTodosDao(): CategoryWithTodosDao
}

private lateinit var INSTANCE: TodoDatabase

fun getDatabase(context: Context): TodoDatabase {
    synchronized(TodoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todos-db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
