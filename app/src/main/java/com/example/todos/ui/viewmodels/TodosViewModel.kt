package com.example.todos.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todos.core.data.TodosRepository
import com.example.todos.core.database.entities.Todo
import com.example.todos.core.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(
    private val todosRepository: TodosRepository
): ViewModel() {
    val todos: Flow<List<Todo>> = todosRepository.todos

    fun toggleTodoDone(todoId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val todo = todosRepository.getTodoById(todoId)
                val newTodo = todo.copy(
                    isDone = !todo.isDone
                )
                todosRepository.updateTodo(newTodo)
            }
        }
    }

    class Factory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodosViewModel(
                    TodosRepository(getDatabase(context))
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}