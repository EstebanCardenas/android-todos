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

    suspend fun addTodo(todo: Todo) {
        database.todoDao().insertTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        database.todoDao().deleteTodo(todo)
    }
}
