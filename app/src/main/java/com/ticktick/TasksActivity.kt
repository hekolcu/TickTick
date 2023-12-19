package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.model.Task

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tasks = ArrayList<Task>()
        val tasksGroupedByDate = tasks.groupBy { it.dueDate }

        val taskGroupAdapter = TaskGroupAdapter(this@TasksActivity, tasksGroupedByDate)
    }
}