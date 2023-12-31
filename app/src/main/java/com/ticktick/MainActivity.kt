package com.ticktick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ticktick.databinding.ActivityMainBinding
import com.ticktick.db.Group
import com.ticktick.db.TaskViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var defaultInbox: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvm = ViewModelProvider(this)[TaskViewModel::class.java]

        binding.btnMainAct.isEnabled = false

        loadData(tvm)

        binding.btnMainAct.setOnClickListener {
            defaultInbox?.let {
                val tasksIntent = Intent(this@MainActivity, TasksActivity::class.java)
                tasksIntent.putExtra("defaultInbox", it)
                startActivity(tasksIntent)
            } ?: run {
                Log.d("MAIN", "defaultInbox is null")
            }
        }
    }

    private fun loadData(tvm: TaskViewModel) {
        lifecycleScope.launch {
            try {
                // Collect first value from flow, which is a list of groups
                val groups = tvm.groupRepository.getGroupsBySearchKey("Inbox").first()

                defaultInbox = if (groups.isNotEmpty()) {
                    groups[0]
                } else {
                    Log.d("MAIN", "No groups found for the search key 'Inbox'")
                    Group("Inbox", 0).also {
                        // Insert new group in a background thread
                        tvm.addGroup(it)
                    }
                }

                // Enable the button in the main thread
                binding.btnMainAct.isEnabled = true
            } catch (e: Exception) {
                Log.e("MAIN", "Error occurred: ${e.message}")
            }
        }
    }
}
