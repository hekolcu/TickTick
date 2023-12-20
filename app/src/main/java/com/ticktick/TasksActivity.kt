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
        tasks.add(Task("", "", Date()))
        val tasksGroupedByDate = tasks.groupBy { it.dueDate }

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        binding.rvTaskGroup.adapter = TaskGroupAdapter(this@TasksActivity, tasksGroupedByDate)
    }
}