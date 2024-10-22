package com.bingugi.todoapp.ui.todolist

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bingugi.todoapp.data.model.Priority
import com.bingugi.todoapp.R
import com.bingugi.todoapp.databinding.TodoHeaderBinding
import com.bingugi.todoapp.databinding.TodoItemBinding
import com.bingugi.todoapp.ui.todolist.TodoListItem.Todo
import com.bingugi.todoapp.ui.todolist.TodoListItem.Header
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class TodoRVAdapter(
    val context: Context,
    val onTodoCheckedChange: (Todo) -> Unit
) : ListAdapter<TodoListItem, RecyclerView.ViewHolder>(TodoDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TASK = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Header-> VIEW_TYPE_HEADER
            is Todo -> VIEW_TYPE_TASK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_HEADER ->{
                val view=TodoHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                TodoHeaderViewHolder(view)
            }
            VIEW_TYPE_TASK -> {
                val view=TodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                TodoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoHeaderViewHolder -> holder.bind(getItem(position)as Header)
            is TodoViewHolder ->{
                val todoItem=getItem(position) as Todo
                holder.bind(todoItem)

                holder.binding.tdCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onTodoCheckedChange(todoItem)
                    }
                }
            }
        }
    }

    inner class TodoHeaderViewHolder(private val binding: TodoHeaderBinding ):RecyclerView.ViewHolder(binding.root){
        fun bind(header:Header){
            val todayDate= LocalDate.now()
            val tomorrowDate=todayDate.plusDays(1)
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
            val todayDateAsString=dateTimeFormatter.format(todayDate)
            val tomorrowDateAsString=dateTimeFormatter.format(tomorrowDate)

            if(header.date==todayDateAsString){
                header.date= getString(context,R.string.tdlistitem_header_today)
            }else if(header.date==tomorrowDateAsString){
                header.date= getString(context,R.string.tdlistitem_header_tomorrow)
            }
            binding.tdHeader.text=header.date
        }
    }
    inner class TodoViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(todo:Todo){
            binding.tdName.text = todo.name
            binding.tdDescription.text=todo.description
            binding.tdCheckbox.isChecked = false


            when (todo.priority) {
                Priority.Urgent -> {
                    setColor(binding.todoCard, R.color.Urgent)
                }

                Priority.High -> {
                    setColor(binding.todoCard, R.color.High)
                }

                Priority.Medium -> {
                    setColor(binding.todoCard, R.color.Medium)
                }

                Priority.Low -> {
                    setColor(binding.todoCard, R.color.Low)
                }
            }
        }
    }
}

private fun setColor(layout: LinearLayout, @ColorRes colorResId: Int) {
    val drawable = ContextCompat.getDrawable(layout.context, R.drawable.layout_bg) as GradientDrawable
    val color = ContextCompat.getColor(layout.context, colorResId)
    drawable.setStroke(10, color)
    layout.background = drawable
}
class TodoDiffCallback : DiffUtil.ItemCallback<TodoListItem>() {
    override fun areItemsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: TodoListItem, newItem: TodoListItem): Boolean = oldItem == newItem
}
