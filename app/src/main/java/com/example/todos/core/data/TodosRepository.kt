package com.example.todos.core.data

import com.example.todos.core.database.TodoDatabase
import com.example.todos.core.database.entities.Todo

class TodosRepository(private val database: TodoDatabase) {
    val todos = database.todoDao().getAllTodos()

    suspend fun updateTodo(todo: Todo) {
        database.todoDao().updateTodo(todo)
    }

    fun getTodoById(id: Int): Todo {
        return database.todoDao().getTodoById(id)
    }
}
