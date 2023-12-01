package com.example.todolist

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolist.databinding.FragmentCheckBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao


class CheckFragment : Fragment() {

    private lateinit var binding : FragmentCheckBinding
    private lateinit var todoDao : ToDoDao
    private lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentCheckBinding.inflate(inflater,container,false)

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())!!
        todoDao = db.getTodoDao()

        Thread{
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY,0)
            today.set(Calendar.MINUTE,0)
            today.set(Calendar.SECOND,0)
            today.set(Calendar.MILLISECOND,0)
            val tomorrowDateMills = today.timeInMillis
            val expiredTodoCount = todoDao.getExpiredTodoCount(tomorrowDateMills)
            activity?.runOnUiThread {
                binding.expiredTodoCount.text = expiredTodoCount.toString()
            }
        }.start()
    }

}