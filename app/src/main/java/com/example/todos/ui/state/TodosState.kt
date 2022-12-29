package com.example.todos.ui.state

data class TodosState(
    val newTodoName: String? = null
) {
    val newTodoNameError: String?
        get() {
            if (newTodoName != null) {
                if (newTodoName.isEmpty()) {
                    return "This field is required"
                }
            }
            return null
        }
    val canAddTodo: Boolean
        get() = newTodoName != null && newTodoNameError == null
}