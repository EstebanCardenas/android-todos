package com.example.todos.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    val name: String
)
