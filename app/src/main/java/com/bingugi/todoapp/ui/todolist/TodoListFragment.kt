package com.bingugi.todoapp.ui.todolist

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bingugi.todoapp.R
import com.bingugi.todoapp.data.model.Todo
import com.bingugi.todoapp.databinding.TodoListFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoListFragment : Fragment() {
    private lateinit var binding: TodoListFragmentBinding
    private lateinit var mAdapter: TodoRVAdapter

    private val todoViewModel by viewModel<TodoListViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodoListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController=findNavController()

        mAdapter= TodoRVAdapter(requireContext()) {
            val todo= Todo(
                name = it.name,
                date = it.date,
                description = it.description,
                priority = it.priority,
            )
            todo.id=it.id
            todoViewModel.deleteTodo(todo)
        }

        observeList()

        binding.fab.setOnClickListener {
            navController.navigate(R.id.AddTodoFragment)
        }

        binding.tdList.layoutManager = LinearLayoutManager(requireContext())
        binding.tdList.adapter = mAdapter

        attachItemTouchHelperToRV()
    }

    private fun observeList(){
        todoViewModel.todos.observe(viewLifecycleOwner) {
            val todoList=todoViewModel.createTodoList()
            mAdapter.submitList(todoList)

            if (todoList.isEmpty()) {
                binding.tdNotasks.visibility = View.VISIBLE
                binding.tdList.visibility = View.GONE
            } else {
                binding.tdNotasks.visibility = View.GONE
                binding.tdList.visibility = View.VISIBLE
            }
        }
    }

    private fun attachItemTouchHelperToRV(){
        val itemTouchHelper=
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val position = viewHolder.adapterPosition
                    val item = mAdapter.currentList[position]

                    return if (item is TodoListItem.Header) {
                        makeMovementFlags(0, 0)
                    } else {
                        makeMovementFlags(0, ItemTouchHelper.LEFT)
                    }
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val todo = mAdapter.currentList[position]
                    if (direction == ItemTouchHelper.LEFT && todo is TodoListItem.Todo) {
                        showDeleteDialog(todo, position)
                    }
                }
            })

        itemTouchHelper.attachToRecyclerView(binding.tdList)
    }

    private fun showDeleteDialog(todoListItem: TodoListItem.Todo, position: Int) {
       val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.td_deletedialog_title))
            .setMessage(getString(R.string.td_deletedialog_text))
            .create()

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.td_deletedialog_positive_text)) { _, _ ->
            val todo= Todo(
                name = todoListItem.name,
                date = todoListItem.date,
                description = todoListItem.description,
                priority = todoListItem.priority
            )
            todoViewModel.deleteTodo(todo)
        }
        alertDialog.setOnCancelListener {
            mAdapter.notifyItemChanged(position)
        }
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.td_deletedialog_negative_text)) { _, _ ->
            mAdapter.notifyItemChanged(position)
        }
        alertDialog.show()
    }

}