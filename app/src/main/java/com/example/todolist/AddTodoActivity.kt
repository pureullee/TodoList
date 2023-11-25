package com.example.todolist



import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import java.text.SimpleDateFormat
import android.widget.Toast
import com.example.todolist.databinding.ActivityAddTodoBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import java.util.Date
import java.util.Locale


class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var db : AppDatabase
    lateinit var todoDao : ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        var isEditMode = intent.getBooleanExtra("isEditMode", false)





        // 현재 날짜 초기화
        binding.date.text = getCurrentDate()

        // 날짜 변경
        binding.dateGroup.setOnClickListener{
            showDatePicker()
        }

        // 수정 모드
        if (isEditMode) {
            initEdit()
        } else {
            //완료 버튼
            binding.btnCompletion.setOnClickListener{
                insertTodo()
            }
        }

    }
    private fun initEdit(){
        val todoItem = intent.getSerializableExtra("todoItem") as ToDoEntity
        // 기존 정보 화면에 넣기
        binding.edtTitle.setText(todoItem.title)

        when (todoItem.importance) {
            1 -> binding.radioGroup.check(R.id.btn_high)
            2 -> binding.radioGroup.check(R.id.btn_middle)
            3 -> binding.radioGroup.check(R.id.btn_low)
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = format.format(todoItem.date)
        binding.date.text = formattedDate

        binding.btnCompletion.setOnClickListener{
            updateTodo(todoItem)
        }
    }

    private fun updateTodo(todoItem : ToDoEntity) {
        val todoTitle = binding.edtTitle.text.toString()
        var todoImportance = binding.radioGroup.checkedRadioButtonId
        val todoDateStr = binding.date.text.toString()

        when (todoImportance) {
            R.id.btn_high -> {
                todoImportance = 1
            }
            R.id.btn_middle -> {
                todoImportance = 2
            }
            R.id.btn_low -> {
                todoImportance = 3
            }
            else -> { todoImportance = -1}
        }

        if (todoImportance == -1 || todoTitle.isBlank()){
            Toast.makeText(this,"모든 항목을 채워주세요.",
                Toast.LENGTH_SHORT).show()
        } else {
            val todoDate = convertStringToDate(todoDateStr)

            Thread {
                todoDao.updateTodo(ToDoEntity(todoItem.id, todoTitle, todoImportance, todoDate))
                runOnUiThread{
                    Toast.makeText(this,"수정되었습니다.",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }

    private fun showDatePicker() {
        // 현재 날짜 가져오기
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        // DatePickerDialog 보여주기
        val datePickerDialog = DatePickerDialog(this, { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            // 날짜가 선택되면 호출되는 콜백 함수
            // 선택된 날짜를 TextView에 표시하거나 필요한 처리를 수행
            binding.date.text = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }, year, month, day)

        // 날짜 제한
        datePickerDialog.datePicker.minDate = calendar.timeInMillis


        datePickerDialog.show()
    }

    private fun getCurrentDate(): String {
        var calendar = Calendar.getInstance()
        return String.format(Locale.getDefault(),"%tF", calendar.time)
    }



    private fun insertTodo() {
        val todoTitle = binding.edtTitle.text.toString()
        var todoImportance = binding.radioGroup.checkedRadioButtonId
        val todoDateStr = binding.date.text.toString()

        when (todoImportance) {
            R.id.btn_high -> {
                todoImportance = 1
            }
            R.id.btn_middle -> {
                todoImportance = 2
            }
            R.id.btn_low -> {
                todoImportance = 3
            }
            else -> { todoImportance = -1}
        }

        if (todoImportance == -1 || todoTitle.isBlank()){
            Toast.makeText(this,"모든 항목을 채워주세요.",
                Toast.LENGTH_SHORT).show()
        } else {
            val todoDate = convertStringToDate(todoDateStr)

            Thread {
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImportance, todoDate))
                runOnUiThread{
                    Toast.makeText(this,"추가되었습니다.",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }

    // 컨버터
    private fun convertStringToDate(dateStr: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateStr)!!
    }

}