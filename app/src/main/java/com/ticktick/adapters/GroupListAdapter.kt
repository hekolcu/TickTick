package com.ticktick.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ticktick.db.Group

class GroupListAdapter(
    private val context: Context
): RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>() {
    private var taskGroupsList: List<Group> = listOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<Group>){
        taskGroupsList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_1,
            viewGroup,
            false
        )
        return GroupViewHolder(itemView)
    }

    override fun onBindViewHolder(groupedTasksViewHolder: GroupViewHolder, position: Int) {
        groupedTasksViewHolder.tv.text = taskGroupsList[position].title
    }

    override fun getItemCount() = taskGroupsList.size

    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        init {
            tv = itemView.findViewById(android.R.id.text1)
        }
    }
}