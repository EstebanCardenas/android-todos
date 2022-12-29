package com.example.todos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todos.core.database.entities.Todo
import com.example.todos.databinding.TodoItemBinding

class TodosAdapter(
    private val onTodoTap: (Int) -> Unit,
    private val onTodoLongTap: (Int) -> Unit
): ListAdapter<Todo, TodosAdapter.TodosViewHolder>(DiffCallback) {
    class TodosViewHolder(
        private val binding: TodoItemBinding,
        private val onTodoTap: (Int) -> Unit,
        private val onTodoLongTap: (Int) -> Unit,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.todoCard.setOnClickListener {
                onTodoTap(todo.id)
            }
            binding.todoCard.setOnLongClickListener {
                onTodoLongTap(todo.id)
                true
            }
            binding.todoCheckbox.isChecked = todo.isDone
            binding.todoTextView.text = todo.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodosViewHolder(binding, onTodoTap, onTodoLongTap)
    }

    override fun onBindViewHolder(holder: TodosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }
    }
}