package com.example.todolist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.ToDoEntity
import android.icu.util.Calendar
import android.view.MotionEvent
import androidx.core.view.isVisible
import java.util.Locale

class TodoRecyclerViewAdapter(private val todoList : ArrayList<ToDoEntity>,
                              private val listener: OnItemClickListener,
                              private val onSwapButtonListener: OnSwapButtonListener
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>(),
    ItemTouchHelperListener
{


    inner class MyViewHolder(binding : ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle
        val tv_date = binding.tvDate
        val tv_tab = binding.editDeleteLayout
        val root = binding.root
        val btn_edit = binding.btnEdit
        val btn_delete = binding.btnDelete
        val tv_bar = binding.tvBar
        val tv_check = binding.tvCheck
        val btn_swap = binding.btnSwap
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
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY,0)
        today.set(Calendar.MINUTE,0)
        today.set(Calendar.SECOND,0)
        today.set(Calendar.MILLISECOND,0)
        holder.tv_tab.visibility = if (todoData.isVisible) View.VISIBLE else View.INVISIBLE

        //check 여부 확인
        if (todoData.isCheck) {
            holder.tv_importance.visibility = View.INVISIBLE
            holder.tv_check.visibility = View.VISIBLE
        } else {
            holder.tv_importance.visibility = View.VISIBLE
            holder.tv_check.visibility = View.GONE
        }
        //time out 관련
        if (todoData.date!!.before(today.time)) {
            holder.tv_importance.setBackgroundResource(R.color.gray)
            holder.tv_importance.isEnabled = false
            holder.tv_check.isEnabled = false
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

        holder.tv_bar.setOnClickListener{
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

        holder.tv_importance.setOnClickListener{
            todoData.isCheck = true
            listener.onCheckClick(position)

        }
        holder.tv_check.setOnClickListener{
            todoData.isCheck = false
            listener.onCheckClick(position)
        }

        holder.btn_swap.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 버튼이 눌린 경우 구현
                    onSwapButtonListener?.onSwapButtonPressed()
                }
                MotionEvent.ACTION_UP -> {
                    // 버튼에서 손을 뗀 경우 구현
                    onSwapButtonListener?.onSwapButtonReleased()
                }
            }
            // true 반환으로써 이벤트 소비를 나타냄
            true
        }

    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean {
        val name = todoList[from_position]
        // 리스트 갱신
        todoList.removeAt(from_position)
        todoList.add(to_position, name)

        // fromPosition에서 toPosition으로 아이템 이동 공지
        notifyItemMoved(from_position, to_position)
        return true
    }

    override fun onItemSwipe(position: Int) {
        TODO("Not yet implemented")
    }

    private fun setTodo(todo : List<ToDoEntity>){

    }


}