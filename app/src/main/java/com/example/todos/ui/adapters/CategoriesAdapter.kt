package com.example.todos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todos.core.database.entities.Category
import com.example.todos.databinding.CategoryCardBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoriesAdapter(
    private val onCategoryTap: ((id: Int) -> Unit)? = null,
    private val onCategoryLongTap: ((id: Int) -> Unit)? = null,
    private val viewLifecycleScope: LifecycleCoroutineScope,
    private val getNumberOfTasksForCategory: ((categoryId: Int) -> Flow<Int>)
): ListAdapter<Category, CategoriesAdapter.CategoriesViewHolder>(DiffCallback) {
    inner class CategoriesViewHolder(
        private val binding: CategoryCardBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.titleText.text = category.name
            binding.root.setOnClickListener {
                onCategoryTap?.invoke(category.id)
            }
            binding.root.setOnLongClickListener {
                onCategoryLongTap?.invoke(category.id)
                true
            }
            viewLifecycleScope.launch {
                getNumberOfTasksForCategory(category.id).collect {
                    val tasksText = if (it == 1) "task" else "tasks"
                    binding.tasksText.text = "$it $tasksText"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = CategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriesViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(
            getItem(position)
        )
    }

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}