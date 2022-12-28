package com.example.todos.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todos.core.database.entities.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

private lateinit var INSTANCE: TodoDatabase

fun getDatabase(context: Context): TodoDatabase {
    synchronized(TodoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todos-db"
            ).build()
        }
    }
    return INSTANCE
}
