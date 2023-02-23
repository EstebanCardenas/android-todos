package com.example.todos.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todos.R
import com.example.todos.core.utils.ModalsHelper
import com.example.todos.databinding.FragmentTodoListBinding
import com.example.todos.ui.adapters.TodosAdapter
import com.example.todos.ui.viewmodels.TodosViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class TodoListFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentTodoListBinding
    private val viewModel: TodosViewModel by activityViewModels {
        TodosViewModel.Factory(requireContext().applicationContext)
    }
    private var categoryId by Delegates.notNull<Int>()

    // Menu config
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        lifecycleScope.launch {
            val category = viewModel.getCategoryById(categoryId)
            if (category.isFavorite) {
                val favoriteAction = menu.findItem(R.id.favorite_action)
                favoriteAction.setIcon(R.drawable.ic_baseline_star_white_24)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.favorite_action -> {
                viewModel.toggleCategoryFavorite(categoryId) {
                    val iconResource = if (it.isFavorite)
                        R.drawable.ic_baseline_star_white_24
                    else
                        R.drawable.ic_baseline_star_border_white_24
                    menuItem.setIcon(iconResource)
                }
                true
            }
            else -> false
        }
    }

    // Lifecycle events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt("categoryId") ?: 1
        requireActivity().addMenuProvider(this)
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

    override fun onPause() {
        super.onPause()
        requireActivity().removeMenuProvider(this)
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