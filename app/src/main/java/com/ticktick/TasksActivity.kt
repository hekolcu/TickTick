package com.ticktick

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ticktick.adapters.TaskGroupAdapter
import com.ticktick.databinding.ActivityTasksBinding
import com.ticktick.db.Task
import com.ticktick.db.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ticktick.adapters.GroupListAdapter
import com.ticktick.backgroundservice.CustomWorker
import com.ticktick.databinding.CreateListDialogBinding
import com.ticktick.databinding.CreateTaskDialogBinding
import com.ticktick.db.Group
import com.ticktick.model.User
import com.ticktick.retrofit.ApiClient
import com.ticktick.util.ApiService

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
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("currentGroup", "HI")

        apiService = ApiClient.getClient().create(ApiService::class.java)

        val workManager = WorkManager.getInstance(applicationContext)

        val request = OneTimeWorkRequest.Builder(CustomWorker::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(request)

        workManager.getWorkInfoByIdLiveData(request.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val jsonUser = workInfo.outputData.getString("json_user")
                    if (jsonUser != null) {
                        val user = Gson().fromJson(jsonUser, User::class.java)
                        updateUserUI(user)
                    }
                }
            }

        currentGroup = intent.getParcelableExtra("defaultInbox", Group::class.java)!!

        Log.d("currentGroup", currentGroup.title)

        binding.tvInboxName.text = currentGroup.title

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
            val taskName = createTaskDialogBinding.tvName.text.toString().trim()
            val descName = createTaskDialogBinding.mLineDescTextView.text.toString().trim()
            // Retrieve the selected group
            val selectedGroup = createTaskDialogBinding.spinnerGroups.selectedItem as Group
            if (taskName.isNotEmpty() && descName.isNotEmpty()) {
                // Use the selected group's ID for the new task
                tvm.addTask(
                    Task(
                        taskName,
                        descName,
                        createTaskSelectedDate,
                        selectedGroup.groupId
                    )
                )
                createTaskDialog.dismiss()
            } else {
                Toast.makeText(this, "Fill the necessary areas !!", Toast.LENGTH_SHORT).show()
            }
        }

        createTaskDialogBinding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            createTaskSelectedDate = dateFormat.format(calendar.time)
        }
        createTaskDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        createTaskDialog.show()
    }

    private fun createAddGroupDialog() {
        createGroupDialog = Dialog(this)

        val createListDialogBinding = CreateListDialogBinding.inflate(layoutInflater)
        createGroupDialog.setContentView(createListDialogBinding.root)

        createListDialogBinding.createListOkBtn.setOnClickListener {
            val listName = createListDialogBinding.createListTextView.text.toString().trim()
            if (listName.isNotEmpty()) {
                Group(listName, 0).also {
                    tvm.addGroup(it)
                }
                createGroupDialog.dismiss()
            } else {
                Toast.makeText(this, "Fill the necessary areas !!", Toast.LENGTH_SHORT).show()
            }
        }
        createGroupDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        createGroupDialog.show()
    }

    private fun updateUserUI(user: User) {
        val profilePhotoUrl = user.profile_photo
        Picasso.get()
            .load(profilePhotoUrl)
            .resize(50, 50)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(binding.listItemImage4)
    }
}