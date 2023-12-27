package com.ticktick

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.db.Task
import com.ticktick.db.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
import com.ticktick.databinding.CreateTaskDialogBinding

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    private lateinit var adapter: TaskGroupAdapter
    private lateinit var createTaskDialog: Dialog
    private lateinit var tvm: TaskViewModel
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var createTaskSelectedDate: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        createTaskSelectedDate = dateFormat.format(Calendar.getInstance().time)

        tvm = ViewModelProvider(this)[TaskViewModel::class.java]

        tvm.readAllTaskData.observe(this) {
            Log.d("task list", it.size.toString())
            val today = Calendar.getInstance()
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }

            val tasksGroupedByDateRef = it.groupBy {
                val taskDate = Calendar.getInstance().apply { time = dateFormat.parse(it.date)!! }

                when {
                    dateFormat.format(taskDate.time) == dateFormat.format(today.time) -> "Today"
                    dateFormat.format(taskDate.time) == dateFormat.format(tomorrow.time) -> "Tomorrow"
                    taskDate.before(today) -> "Past" // Group as "Past" if the date is before today
                    else -> dateFormat.format(taskDate.time)
                }
            }
            Log.d("grouped_tasks Today", tasksGroupedByDateRef["Today"]?.size.toString())
            Log.d("grouped_tasks Tomorrow", tasksGroupedByDateRef["Tomorrow"]?.size.toString())
            Log.d("grouped_tasks Past", tasksGroupedByDateRef["Past"]?.size.toString())
            adapter.setData(tasksGroupedByDateRef)

            binding.listItemTitle4.setOnClickListener {
                val intent = Intent(this, UserActivity::class.java)

                startActivity(intent)
            }
        }

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        adapter = TaskGroupAdapter(this@TasksActivity)
        binding.rvTaskGroup.adapter = adapter

        binding.crtTaskFloatActBtn.setOnClickListener {
            createAddTaskDialog()
        }

        binding.ivMenu.setOnClickListener {
            toggleLeftMenu(binding.leftMenu)
        }
    }

    private fun toggleLeftMenu(leftMenu: View) {
        if (leftMenu.visibility == View.VISIBLE) {
            leftMenu.visibility = View.INVISIBLE
        } else {
            leftMenu.visibility = View.VISIBLE
        }
    }


    private fun createAddTaskDialog() {
        createTaskDialog = Dialog(this)

        val createTaskDialogBinding = CreateTaskDialogBinding.inflate(layoutInflater)
        createTaskDialog.setContentView(createTaskDialogBinding.root)

        createTaskDialogBinding.okBtn.setOnClickListener {
            Log.d("date", createTaskSelectedDate)
            tvm.addTask(Task(
                createTaskDialogBinding.tvName.text.toString(),
                createTaskDialogBinding.mLineDescTextView.text.toString(),
                createTaskSelectedDate
            ))
            createTaskDialog.dismiss()
        }

        createTaskDialogBinding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            createTaskSelectedDate = dateFormat.format(calendar.time)
        }

        createTaskDialog.show()
    }
}