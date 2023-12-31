package com.ticktick

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
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
    private val CHANNEL_ID = "TICKTICKCHANNELID"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val tvm = ViewModelProvider(this)[TaskViewModel::class.java]

        binding.btnMainAct.isEnabled = false

        loadData(tvm)

        binding.btnMainAct.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.activity_sound)
            val tasksIntent = Intent(this@MainActivity, TasksActivity::class.java)

            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }

            defaultInbox?.let {
                tasksIntent.putExtra("defaultInbox", it)
                startActivity(tasksIntent)
                sendNotification("Welcome", "Welcome to TickTick!")
            } ?: run {
                Log.d("MAIN", "defaultInbox is null")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TickTick Channel"
            val descriptionText = "TickTick Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
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