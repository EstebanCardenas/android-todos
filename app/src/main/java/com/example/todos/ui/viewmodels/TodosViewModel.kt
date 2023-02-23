package com.example.todos.ui.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.todos.core.data.CategoriesRepository
import com.example.todos.core.data.TodosRepository
import com.example.todos.core.database.entities.Category
import com.example.todos.core.database.entities.Todo
import com.example.todos.core.database.getDatabase
import com.example.todos.core.database.relations.CategoryWithTodos
import com.example.todos.ui.state.TodosState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(
    private val todosRepository: TodosRepository,
    private val categoriesRepository: CategoriesRepository,
): ViewModel() {
    val categories: Flow<List<Category>> = categoriesRepository.categories
    val activeTodos: Flow<List<Todo>> = todosRepository.getActiveTodos()

    private val _uiState = MutableStateFlow(TodosState())
    val uiState: StateFlow<TodosState> = _uiState.asStateFlow()

    fun getTodosByCategoryId(categoryId: Int): Flow<CategoryWithTodos> {
        return todosRepository.getTodosByCategoryId(categoryId)
    }

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

    fun addTodo(categoryId: Int) {
        viewModelScope.launch {
            todosRepository.addTodo(Todo(
                name = _uiState.value.newTodoName!!,
                isDone = false,
                categoryId = categoryId
            ))
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val todo = todosRepository.getTodoById(id)
                todosRepository.deleteTodo(todo)
            }
        }
    }

    fun createCategory() {
        viewModelScope.launch {
            categoriesRepository.createCategory(Category(
                name = _uiState.value.newTodoName!!,
            ))
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val category: Category = categoriesRepository.getCategoryById(categoryId)
                categoriesRepository.deleteCategory(category)
            }
        }
    }

    fun getNumberOfTasksForCategory(categoryId: Int): Flow<Int> {
        return categoriesRepository.getNumberOfTasksForCategory(categoryId)
    }

    suspend fun getCategoryById(id: Int): Category {
        return categoriesRepository.getCategoryById(id)
    }

    fun toggleCategoryFavorite(
        categoryId: Int,
        onCategoryUpdated: ((category: Category) -> Unit)?
    ) {
        viewModelScope.launch {
            var category = getCategoryById(categoryId)
            category = category.copy(
                isFavorite = !category.isFavorite
            )
            categoriesRepository.updateCategory(category)
            onCategoryUpdated?.invoke(category)
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
                    TodosRepository(getDatabase(context)),
                    CategoriesRepository(getDatabase(context)),
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
