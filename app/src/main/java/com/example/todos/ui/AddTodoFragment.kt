package com.example.todos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todos.MainActivity
import com.example.todos.R
import com.example.todos.databinding.FragmentAddTodoBinding
import com.example.todos.ui.viewmodels.TodosViewModel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AddTodoFragment : Fragment() {
    private val viewModel: TodosViewModel by activityViewModels {
        TodosViewModel.Factory(requireContext().applicationContext)
    }
    private lateinit var binding: FragmentAddTodoBinding
    private var categoryId: Int? = null
    private val shouldCreateCategory
        get() = categoryId == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getString("categoryId")?.toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddTodoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLabels()
        binding.todoEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onNewTodoNameChanged(text.toString())
        }
        binding.addButton.setOnClickListener { onAddTap() }
        lifecycleScope.launch {
            viewModel.uiState.collect {
                binding.todoEditText.error = it.newTodoNameError
                binding.addButton.isEnabled = it.canAddTodo
            }
        }
    }

    private fun onAddTap() {
        if (shouldCreateCategory) {
            viewModel.createCategory()
        } else {
            viewModel.addTodo(categoryId!!)
        }
        findNavController().popBackStack()
    }

    private fun updateLabels() {
        if (shouldCreateCategory) {
            binding.title.text = "Create Category"
            binding.todoEditText.hint = binding.todoEditText.hint.replace(
                Regex("TODO"),
                "category"
            )
            (requireActivity() as MainActivity).supportActionBar?.title =
                "Add Category"
        }
    }
}