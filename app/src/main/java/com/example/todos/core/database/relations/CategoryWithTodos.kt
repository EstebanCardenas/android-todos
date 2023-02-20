package com.example.todos.core.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todos.core.database.entities.Category
import com.example.todos.core.database.entities.Todo

data class CategoryWithTodos(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    ) val todos: List<Todo>
)
