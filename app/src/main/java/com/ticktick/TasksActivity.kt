package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.db.Task
import com.ticktick.db.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.ViewModelProvider


class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    private lateinit var adapter: TaskGroupAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvm = ViewModelProvider(this)[TaskViewModel::class.java]

        tvm.readAllTaskData.observe(this) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
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
            adapter.setData(tasksGroupedByDateRef)
        }

        tvm.addTask(Task("my task", "description", "2023-12-27"))

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        adapter = TaskGroupAdapter(this@TasksActivity)
        binding.rvTaskGroup.adapter = adapter
    }
}