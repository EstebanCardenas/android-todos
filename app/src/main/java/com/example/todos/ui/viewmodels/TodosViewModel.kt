package com.example.todos.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.todos.core.data.TodosRepository
import com.example.todos.core.database.entities.Todo
import com.example.todos.core.database.getDatabase
import com.example.todos.ui.state.TodosState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(
    private val todosRepository: TodosRepository
): ViewModel() {
    val todos: Flow<List<Todo>> = todosRepository.todos

    private val _uiState = MutableStateFlow<TodosState>(TodosState())
    val uiState: StateFlow<TodosState> = _uiState.asStateFlow()

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

    fun onNewTodoNameChanged(value: String) {
        _uiState.update {
            it.copy(
                newTodoName = value
            )
        }
    }

    fun addTodo() {
        viewModelScope.launch {
            todosRepository.addTodo(Todo(
                name = _uiState.value.newTodoName!!,
                isDone = false
            ))
        }
    }

    fun clearState() {
        _uiState.update {
            TodosState()
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