package com.ticktick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ticktick.databinding.ActivityMainBinding
import com.ticktick.db.TaskViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainAct.setOnClickListener {
            val tasksIntent = Intent(this@MainActivity, TasksActivity::class.java)
            startActivity(tasksIntent)
        }
    }
}