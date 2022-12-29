package com.example.todos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todos.R
import com.example.todos.databinding.FragmentTodoListBinding
import com.example.todos.ui.adapters.TodosAdapter
import com.example.todos.ui.viewmodels.TodosViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodosViewModel by activityViewModels {
        TodosViewModel.Factory(requireContext().applicationContext)
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

        val adapter = TodosAdapter(
            onTodoTap =  viewModel::toggleTodoDone,
            onTodoLongTap = ::showTodoDeletionPrompt
        )
        binding.todoRecyclerView.adapter = adapter
        binding.addFab.setOnClickListener {
            viewModel.clearState()
            findNavController().navigate(R.id.action_todoListFragment_to_addTodoFragment)
        }
        lifecycleScope.launch {
            viewModel.todos.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun showTodoDeletionPrompt(id: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete TODO")
            .setMessage("Are you sure you want to delete this TODO?")
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("YES") { dialog, _ ->
                viewModel.deleteTodo(id)
                dialog.dismiss()
            }
            .show()
    }
}