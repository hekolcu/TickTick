package com.ticktick.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
it provides data to the UI and survive configuration changes. It acts as a communication center between repository and the UI
 */
class TaskViewModel(application:Application):AndroidViewModel(application) {
    val readAllData: LiveData<List<Task>>
    private val repository:TaskRepository
    init {
        val taskDAO= TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDAO)
        readAllData = repository.readAlldata
    }
    fun addTask(task:Task){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository.insertTask(task)
        }
    }
    fun addTasks(tasks: List<Task>){
        viewModelScope.launch(Dispatchers.IO) { // that code will be run in background thread, coroutine scope
            tasks.forEach{
                repository.insertTask(it)
            }
        }
    }
    fun deleteTask(task:Task){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository.deleteTask(task)
        }
    }
    fun deleteAllTask(){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository.deleteAllTasks()
        }
    }
//    fun updateTask(task:Task){
//        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
//            repository.updateTask(task)
//        }
//    }
    fun searchTask(searchkey:String):LiveData<List<Task>>{
            return repository.getTasksBySearchKey(searchkey).asLiveData()
    }
}