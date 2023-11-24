package com.example.todolist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.ToDoEntity
import java.util.Calendar
import java.util.Locale

class TodoRecyclerViewAdapter(private val todoList : ArrayList<ToDoEntity>, private val listener: OnItemClickListener) : RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>(){


    inner class MyViewHolder(binding : ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle
        val tv_date = binding.tvDate
        val tv_tab = binding.editDeleteLayout
        val root = binding.root
        val btn_edit = binding.btnEdit
        val btn_delete = binding.btnDelete
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding : ItemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = todoList[position]
        val currentDate = Calendar.getInstance().time

        holder.tv_tab.visibility = if (todoData.isVisible) View.VISIBLE else View.INVISIBLE
        //time out 관련
        if (todoData.date!!.before(currentDate)) {
            holder.tv_importance.setBackgroundResource(R.color.gray)
        } else {
            when (todoData.importance) {
                1 -> {
                    holder.tv_importance.setBackgroundResource(R.color.red)
                }

                2 -> {
                    holder.tv_importance.setBackgroundResource(R.color.yellow)
                }

                3 -> {
                    holder.tv_importance.setBackgroundResource(R.color.green)
                }
            }
        }
        holder.tv_importance.text = todoData.importance.toString()
        holder.tv_title.text = todoData.title
        holder.tv_date.text = String.format(Locale.getDefault(),"%tF", todoData.date)

        holder.root.setOnClickListener{
            //토글
            todoData.isVisible = !todoData.isVisible

            holder.tv_tab.visibility = if (todoData.isVisible) View.VISIBLE else View.INVISIBLE

        }

        holder.btn_edit.setOnClickListener{
            listener.onEditClick(position)
        }
        holder.btn_delete.setOnClickListener{
            listener.onDeleteClick(position)
        }

    }



    override fun getItemCount(): Int {
        return todoList.size
    }
}