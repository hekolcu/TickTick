package com.ticktick.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ticktick.R
import com.ticktick.model.Task

class TaskListAdapter(context: Context, taskList: List<Task>): ArrayAdapter<Task>(context, R.layout.task_view_holder, taskList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task: Task = getItem(position)!!
        var currentView = convertView
        if (currentView == null){
            currentView = LayoutInflater.from(context).inflate(R.layout.task_view_holder, parent, false)
        }

        val title = currentView?.findViewById<TextView>(R.id.tv_taskTitle)
        val description = currentView?.findViewById<TextView>(R.id.tv_taskDescription)

        title?.text = task.name
        description?.text = task.description

        return currentView!!
    }
}