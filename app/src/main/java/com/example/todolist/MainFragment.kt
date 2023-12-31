package com.example.todolist

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider


class MainFragment : Fragment(), OnItemClickListener, OnSwapButtonListener {
    private lateinit var binding :FragmentMainBinding
    private lateinit var db : AppDatabase
    private lateinit var todoDao : ToDoDao
    private lateinit var todoList : ArrayList<ToDoEntity>
    private lateinit var adapter: TodoRecyclerViewAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemTouchHelperCallback: ItemTouchHelperCallback
    private var isSwapEnabled = false

    private lateinit var sharedViewModel: SharedViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(activity, AddTodoActivity::class.java)
            startActivity(intent)
        }
        Log.d("test", "hi")
        db = AppDatabase.getInstance(requireContext())!!
        todoDao = db.getTodoDao()
        getAllTodoList()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.deletedItems.observe(viewLifecycleOwner, { deletedItems ->
            println("작동")
            // 삭제된 아이템이 변경되었을 때 UI 갱신
            Log.d("LiveDataDebug", "Deleted Items: $deletedItems")
            if (deletedItems.isNotEmpty()) {
                println("왜안됨")
            }
        })
    }

    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAll())
            setRecyclerView()
        }.start()
    }
    private fun setRecyclerView() {
        activity?.runOnUiThread {
            adapter = TodoRecyclerViewAdapter(todoList, this, this )
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
            itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        }

    }

    override fun onStart() {
        super.onStart()
        getAllTodoList()
    }

    override fun onEditClick(position: Int) {
        val intent = Intent(activity, AddTodoActivity::class.java)
        intent.putExtra("isEditMode", true)
        intent.putExtra("todoItem", todoList[position])
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
                    deleteTodo(position)
                }
            })
        builder.show()
    }
    private fun deleteTodo(position: Int) {
        Thread {
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    override fun onCheckClick(position: Int) {
        Thread {
            todoDao.updateTodo(todoList[position])
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