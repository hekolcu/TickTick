package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.model.Task
import java.util.Date

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tasks = ArrayList<Task>()
        tasks.add(Task("MY FIRST TASK", "Hello", Date(2023, 12, 15, 0, 0, 0)))
        tasks.add(Task("MY Second TASK", "Hello", Date(2023, 12, 15, 0, 0, 0)))
        tasks.add(Task("MY Third TASK", "Hello", Date(2023, 12, 15, 0, 0, 0)))
        tasks.add(Task("MY Fourth TASK", "Hello", Date(2023, 12, 15, 0, 0, 0)))

        tasks.add(Task("askjdlas", "Hello", Date(2023, 12, 15, 1, 0, 0)))
        tasks.add(Task("asjdkas", "Hello", Date(2023, 12, 15, 1, 0, 0)))
        val tasksGroupedByDate = tasks.groupBy { it.dueDate }

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        binding.rvTaskGroup.adapter = TaskGroupAdapter(this@TasksActivity, tasksGroupedByDate)
    }
}