package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.model.Task
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tasks = ArrayList<Task>()
        tasks.add(Task("HI", "Hello", Date(2023 - 1900, 12 - 1, 26, 1, 0, 0)))
        tasks.add(Task("HI 2", "Hello", Date(2023 - 1900, 12 - 1, 27, 1, 0, 0)))

        tasks.add(Task("MY FIRST TASK", "Hello", Date(2023 - 1900, 12 - 1, 15, 0, 0, 0)))
        tasks.add(Task("MY Second TASK", "Hello", Date(2023 - 1900, 12 - 1, 15, 0, 0, 0)))
        tasks.add(Task("MY Third TASK", "Hello", Date(2023 - 1900, 12 - 1, 15, 0, 0, 0)))
        tasks.add(Task("MY Fourth TASK", "Hello", Date(2023 - 1900, 12 - 1, 15, 0, 0, 0)))

        tasks.add(Task("askjdlas", "Hello", Date(2023 - 1900, 12 - 1, 15, 1, 0, 0)))
        tasks.add(Task("asjdkas", "Hello", Date(2023 - 1900, 12 - 1, 15, 1, 0, 0)))

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val today = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }

        val tasksGroupedByDateRef = tasks.groupBy {
            val taskDate = Calendar.getInstance().apply { time = it.dueDate }

            when {
                dateFormat.format(taskDate.time) == dateFormat.format(today.time) -> "Today"
                dateFormat.format(taskDate.time) == dateFormat.format(tomorrow.time) -> "Tomorrow"
                else -> dateFormat.format(taskDate.time)
            }
        }

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        binding.rvTaskGroup.adapter = TaskGroupAdapter(this@TasksActivity, tasksGroupedByDateRef)
    }
}