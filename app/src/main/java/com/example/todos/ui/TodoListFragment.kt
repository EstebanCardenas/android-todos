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
import com.example.todos.databinding.FragmentTodoListBinding
import com.example.todos.ui.adapters.TodosAdapter
import com.example.todos.ui.viewmodels.TodosViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodosViewModel by activityViewModels {
        TodosViewModel.Factory(requireContext().applicationContext)
    }
    private var categoryId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt("categoryId") ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLabels()
        val adapter = TodosAdapter(
            onTodoTap =  viewModel::toggleTodoDone,
            onTodoLongTap = ::showTodoDeletionPrompt
        )
        binding.todoRecyclerView.adapter = adapter
        binding.addFab.setOnClickListener {
            navigateToTodoCreation()
        }
        lifecycleScope.launch {
            viewModel.getTodosByCategoryId(categoryId).collect {
                adapter.submitList(it.todos)
            }
        }
    }

    private fun updateLabels() {
        lifecycleScope.launch {
            val categoryName = viewModel.getCategoryById(categoryId).name
            binding.titleTextView.text = "TODOs for $categoryName"
        }
    }

    private fun showTodoDeletionPrompt(id: Int) {
        ModalsHelper.showTodoDeletionModal(requireContext()) {
            viewModel.deleteTodo(id)
        }
    }

    private fun navigateToTodoCreation() {
        viewModel.clearState()
        val bundle = bundleOf("categoryId" to categoryId.toString())
        findNavController().navigate(R.id.action_todoListFragment_to_addTodoFragment, bundle)
    }
}