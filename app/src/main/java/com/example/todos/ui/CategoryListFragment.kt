package com.example.todos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todos.R
import com.example.todos.core.utils.ModalsHelper
import com.example.todos.databinding.FragmentCategoryListBinding
import com.example.todos.ui.adapters.CategoriesAdapter
import com.example.todos.ui.adapters.TodosAdapter
import com.example.todos.ui.viewmodels.TodosViewModel
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding
    private val viewModel: TodosViewModel by activityViewModels {
        TodosViewModel.Factory(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCategoryListBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoriesAdapter()
        setupTodosAdapter()
        binding.addFab.setOnClickListener { navigateToCategoryCreation() }

    }

    private fun setupCategoriesAdapter() {
        val categoriesAdapter = CategoriesAdapter(
            viewLifecycleScope = lifecycleScope,
            getNumberOfTasksForCategory = viewModel::getNumberOfTasksForCategory,
            onCategoryTap = ::navigateToTasksFragment,
            onCategoryLongTap = ::showCategoryDeletionPrompt,
        )
        binding.categoriesRecyclerView.adapter = categoriesAdapter
        lifecycleScope.launch {
            viewModel.categories.collect {
                if (it.isNotEmpty()) {
                    binding.mainContent.visibility = View.VISIBLE
                    binding.emptyState.visibility = View.GONE
                } else {
                    binding.mainContent.visibility = View.GONE
                    binding.emptyState.visibility = View.VISIBLE
                }
                categoriesAdapter.submitList(it)
            }
        }
    }

    private fun setupTodosAdapter() {
        val todosAdapter = TodosAdapter(
            onTodoTap = viewModel::toggleTodoDone,
            onTodoLongTap = ::showTodoDeletionPrompt,
        )
        binding.todoRecyclerView.adapter = todosAdapter
        lifecycleScope.launch {
            viewModel.activeTodos.collect {
                if (it.isNotEmpty()) {
                    binding.activeTasks.visibility = View.VISIBLE
                } else {
                    binding.activeTasks.visibility = View.GONE
                }
                todosAdapter.submitList(it)
            }
        }
    }

    private fun showCategoryDeletionPrompt(id: Int) {
        ModalsHelper.showCategoryDeletionModal(requireContext()) {
            viewModel.deleteCategory(id)
        }
    }

    private fun showTodoDeletionPrompt(id: Int) {
        ModalsHelper.showTodoDeletionModal(requireContext()) {
            viewModel.deleteTodo(id)
        }
    }

    private fun navigateToTasksFragment(categoryId: Int) {
        val bundle = bundleOf("categoryId" to categoryId)
        findNavController().navigate(R.id.action_categoryListFragment_to_todoListFragment, bundle)
    }

    private fun navigateToCategoryCreation() {
        viewModel.clearState()
        findNavController().navigate(R.id.action_categoryListFragment_to_addTodoFragment)
    }
}