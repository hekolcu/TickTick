package com.ticktick.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ticktick.R
import com.ticktick.db.Task

class TaskGroupAdapter(
    private val context: Context
): RecyclerView.Adapter<TaskGroupAdapter.GroupedTasksViewHolder>() {
    private var groupedTasksMap: Map<String, List<Task>> = mapOf()
    fun setData(items: Map<String, List<Task>>){
        groupedTasksMap = items
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GroupedTasksViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.grouped_tasks_view_holder,
            viewGroup,
            false
        )
        return GroupedTasksViewHolder(itemView)
    }

    override fun onBindViewHolder(groupedTasksViewHolder: GroupedTasksViewHolder, position: Int) {
        val date = groupedTasksMap.keys.toList()[position]
        groupedTasksViewHolder.header.text = date

        for (task in groupedTasksMap[date]!!) {
            val itemView = LayoutInflater.from(context).inflate(
                R.layout.task_view_holder,
                groupedTasksViewHolder.tasksLinearLayout,
                false
            )
            val taskViewHolder = TaskViewHolder(itemView)
            taskViewHolder.title.text = task.name
            taskViewHolder.description.text = task.desc
            groupedTasksViewHolder.tasksLinearLayout.addView(itemView)
        }
    }

    override fun getItemCount() = groupedTasksMap.keys.size

    inner class GroupedTasksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var header: TextView
        var tasksLinearLayout: LinearLayout
        init {
            header = itemView.findViewById(R.id.tv_groupedTasksHeader)
            tasksLinearLayout = itemView.findViewById(R.id.ll_tasks)
        }
    }

    inner class TaskViewHolder(itemView: View) {
        var title: TextView
        var description: TextView
        init {
            title = itemView.findViewById(R.id.tv_taskTitle)
            description = itemView.findViewById(R.id.tv_taskDescription)
        }
    }
}