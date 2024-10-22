package com.bingugi.todoapp.ui.addtodo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bingugi.todoapp.data.model.Priority
import com.bingugi.todoapp.R
import com.bingugi.todoapp.data.model.Todo
import com.bingugi.todoapp.databinding.AddTodoFragmentBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AddTodoFragment : Fragment() {

    private lateinit var navController: NavController

    private val todoViewModel by viewModel<AddTodoViewModel>()
    private lateinit var binding: AddTodoFragmentBinding
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddTodoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController=findNavController()

        val priorityItems= listOf(
            getString(R.string.ddmenu_priority_urgent),
            getString(R.string.ddmenu_priority_high),
            getString(R.string.ddmenu_priority_medium),
            getString(R.string.ddmenu_priority_low),
        )

        setTextWatchers()

        binding.tdAddDateLayout.setEndIconOnClickListener {
            showDatePickerDialog()
            binding.tdAddNameLayout.clearFocus()
            binding.tdAddDescription.clearFocus()
            binding.tdAddDateLayout.isFocusable=true
            binding.tdAddDateLayout.requestFocus()
        }

        adapter=ArrayAdapter<String>(requireContext(), R.layout.ddmenu_item,priorityItems)
        binding.tdAddPriority.setAdapter(adapter)

        binding.button.setOnClickListener {
            val name = binding.tdAddName.text.toString()
            val dateString = binding.tdAddDate.text.toString()
            val description = binding.tdAddDescription.text.toString()
            val priority = binding.tdAddPriority.text.toString()

                    val todo = Todo(
                    name,
                    dateString,
                    description,
                    priority.toEnum()
                )
                todoViewModel.addTodo(todo)
                navController.navigate(R.id.TodoListFragment)
        }
    }

    private fun showDatePickerDialog() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(today)
            .setValidator(DateValidatorPointForward.from(today))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.td_datepicker_title))
            .setSelection(today)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selection)
            binding.tdAddDate.setText(selectedDate)

            binding.tdAddDate.clearFocus()
            binding.tdAddDateLayout.requestFocus()
        }

        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun setTextWatchers(){
         val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(checkIfEntered()){
                    binding.button.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.tdAddName.addTextChangedListener(textWatcher)
        binding.tdAddDate.addTextChangedListener(textWatcher)
        binding.tdAddPriority.addTextChangedListener(textWatcher)
    }


    private fun checkIfEntered(): Boolean {
        val name = binding.tdAddName.text
        val date = binding.tdAddDate.text
        val priority = binding.tdAddPriority.text

        val isAllFieldsFilled = !name.isNullOrEmpty() && !date.isNullOrEmpty() && !priority.isNullOrEmpty()
        binding.button.isEnabled = isAllFieldsFilled
        return isAllFieldsFilled
    }
    private fun String.toEnum(): Priority {
        val context= requireContext()
        val stringAsPriority: Priority =when(this){
            getString(context,R.string.ddmenu_priority_urgent)-> Priority.Urgent
            getString(context,R.string.ddmenu_priority_high)-> Priority.High
            getString(context,R.string.ddmenu_priority_medium) -> Priority.Medium
            getString(context,R.string.ddmenu_priority_low)-> Priority.Low
            else ->{
                throw IllegalStateException("Invalid value of priority $this")
            }
        }

        return stringAsPriority
    }

}




