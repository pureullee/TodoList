package com.example.todolist


import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentCalendarBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import java.util.Date


class CalendarFragment : Fragment(),OnItemClickListener, OnSwapButtonListener {

    private lateinit var binding : FragmentCalendarBinding
    private lateinit var todoDao : ToDoDao
    private lateinit var listByselectedDate : ArrayList<ToDoEntity>
    private lateinit var adapter: TodoRecyclerViewAdapter
    private lateinit var db : AppDatabase
    private var isSwapEnabled = false

    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())!!
        todoDao = db.getTodoDao()


        binding.cal.setOnDateChangeListener { _, year, month, dayOfMonth ->
            var selectedDate = Calendar.getInstance().apply{
                set(year,month,dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            updateRecyclerViewForSelectedDate(selectedDate)
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    }

    private fun updateRecyclerViewForSelectedDate(selectedDate : Date) {
        Thread {
            listByselectedDate = ArrayList(todoDao.getTodoByDate(selectedDate))
            activity?.runOnUiThread{
                adapter = TodoRecyclerViewAdapter(listByselectedDate, this, this )
                binding.calRecyclerView.adapter = adapter
                binding.calRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }.start()
    }

    override fun onEditClick(position: Int) {
        val intent = Intent(activity, AddTodoActivity::class.java)
        intent.putExtra("isEditMode", true)
        intent.putExtra("todoItem", listByselectedDate[position])
        startActivity(intent)
    }

    override fun onDeleteClick(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("할 일 삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("네",
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    sharedViewModel.setDeletedItems(listOf(listByselectedDate[position]))
                    deleteTodo(position)
                }
            })
        builder.show()

    }
    private fun deleteTodo(position: Int) {
        Thread {
            todoDao.deleteTodo(listByselectedDate[position])
            listByselectedDate.removeAt(position)
            activity?.runOnUiThread {
                Log.d("DeleteDebug", "Item deleted at position $position")
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    override fun onCheckClick(position: Int) {
        Thread {
            todoDao.updateTodo(listByselectedDate[position])
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    override fun onSwapButtonPressed() {
        isSwapEnabled = true
    }

    override fun onSwapButtonReleased() {
        isSwapEnabled = false
    }

}