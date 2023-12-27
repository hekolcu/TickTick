package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.model.Task
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



        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val today = Calendar.getInstance()
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }

        val tasksGroupedByDateRef = tasks.groupBy {
            val taskDate = Calendar.getInstance().apply { time = it.dueDate }

            when {
                dateFormat.format(taskDate.time) == dateFormat.format(today.time) -> "Today"
                dateFormat.format(taskDate.time) == dateFormat.format(tomorrow.time) -> "Tomorrow"
                taskDate.before(today) -> "Past" // Group as "Past" if the date is before today
                else -> dateFormat.format(taskDate.time)
            }
        }

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        binding.rvTaskGroup.adapter = TaskGroupAdapter(this@TasksActivity, tasksGroupedByDateRef)
    }
}