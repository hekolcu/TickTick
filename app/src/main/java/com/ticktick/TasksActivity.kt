package com.ticktick

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.db.Task
import com.ticktick.db.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
import com.ticktick.adapters.GroupListAdapter
import com.ticktick.databinding.CreateListDialogBinding
import com.ticktick.databinding.CreateTaskDialogBinding
import com.ticktick.db.Group

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    private lateinit var taskGroupAdapter: TaskGroupAdapter
    private lateinit var groupListAdapter: GroupListAdapter
    private lateinit var createTaskDialog: Dialog
    private lateinit var createGroupDialog: Dialog
    private lateinit var tvm: TaskViewModel
    private lateinit var dateFormat: SimpleDateFormat
    private lateinit var createTaskSelectedDate: String
    private lateinit var currentGroup: Group
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("currentGroup", "HI")

        currentGroup = intent.getParcelableExtra("defaultInbox", Group::class.java)!!

        Log.d("currentGroup", currentGroup.title)

        dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        createTaskSelectedDate = dateFormat.format(Calendar.getInstance().time)

        tvm = ViewModelProvider(this)[TaskViewModel::class.java]

        val layoutManager = LinearLayoutManager(this@TasksActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroup.layoutManager = layoutManager

        val layoutManager2 = LinearLayoutManager(this@TasksActivity)
        layoutManager2.orientation = LinearLayoutManager.VERTICAL
        binding.rvTaskGroups.layoutManager = layoutManager2

        taskGroupAdapter = TaskGroupAdapter(this@TasksActivity)
        binding.rvTaskGroup.adapter = taskGroupAdapter

        groupListAdapter = GroupListAdapter(this@TasksActivity)
        binding.rvTaskGroups.adapter = groupListAdapter


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
            taskGroupAdapter.setData(tasksGroupedByDateRef)
        }

        tvm.readAllGroupData.observe(this) {
            groupListAdapter.setData(it)
        }

        binding.crtTaskFloatActBtn.setOnClickListener {
            createAddTaskDialog()
        }

        binding.listItemTitle4.setOnClickListener {
            val userActivityIntent = Intent(this, UserActivity::class.java)
            startActivity(userActivityIntent)
        }

        binding.ivMenu.setOnClickListener {
            toggleLeftMenu(binding.leftMenu)
        }

        binding.llAddItem.setOnClickListener {
            createAddGroupDialog()
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

        // Set up the spinner and its adapter
        val groupsAdapter = ArrayAdapter<Group>(this, android.R.layout.simple_spinner_item)
        createTaskDialogBinding.spinnerGroups.adapter = groupsAdapter

        // Observe the group data and update the spinner
        tvm.groupRepository.getAllGroups().observe(this) { groups ->
            groupsAdapter.clear()
            groupsAdapter.addAll(groups)
            groupsAdapter.notifyDataSetChanged()
        }

        createTaskDialogBinding.okBtn.setOnClickListener {
            // Retrieve the selected group
            val selectedGroup = createTaskDialogBinding.spinnerGroups.selectedItem as Group
            // Use the selected group's ID for the new task
            tvm.addTask(Task(
                createTaskDialogBinding.tvName.text.toString(),
                createTaskDialogBinding.mLineDescTextView.text.toString(),
                createTaskSelectedDate,
                selectedGroup.groupId
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

    private fun createAddGroupDialog() {
        createGroupDialog = Dialog(this)

        val createListDialogBinding = CreateListDialogBinding.inflate(layoutInflater)
        createGroupDialog.setContentView(createListDialogBinding.root)

        createListDialogBinding.createListOkBtn.setOnClickListener {
            Group(createListDialogBinding.createListTextView.text.toString(), 0).also {
                tvm.addGroup(it)
            }
            createGroupDialog.dismiss()
        }

        createGroupDialog.show()
    }
}