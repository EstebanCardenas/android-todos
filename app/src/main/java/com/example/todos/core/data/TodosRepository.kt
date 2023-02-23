package com.example.todos.core.data

import com.example.todos.core.database.TodoDatabase
import com.example.todos.core.database.entities.Todo
import com.example.todos.core.database.relations.CategoryWithTodos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodosRepository(private val database: TodoDatabase) {
    fun getTodosByCategoryId(categoryId: Int): Flow<CategoryWithTodos> {
        val categoryWithTodosFlow: Flow<CategoryWithTodos> =
            database.categoryWithTodosDao().getTodosFromCategoryId(categoryId)
        return categoryWithTodosFlow.map {
            val sortedTodos: List<Todo> = listOf(*it.todos.toTypedArray())
                .sortedBy { todo -> todo.isDone }
            it.copy(
                todos = sortedTodos,
            )
        }
    }

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

    fun getActiveTodos(): Flow<List<Todo>> {
        return database.todoDao().getActiveTodos()
    }
}
