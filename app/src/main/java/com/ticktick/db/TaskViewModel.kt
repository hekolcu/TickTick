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

    val readAllData2: LiveData<List<Group>>
    private val repository2:GroupRepository
    init {
        val taskDAO= TaskRoomDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDAO)
        readAllData = repository.readAlldata

        val groupDAO= TaskRoomDatabase.getDatabase(application).groupDao()
        repository2 = GroupRepository(groupDAO)
        readAllData2 = repository2.readAlldata
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

//Group

    fun addGroup(group:Group){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository2.insertGroup(group)
        }
    }
    fun addGroups(groups: List<Group>){
        viewModelScope.launch(Dispatchers.IO) { // that code will be run in background thread, coroutine scope
            groups.forEach{
                repository2.insertGroup(it)
            }
        }
    }
    fun deleteGroup(group: Group){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository2.deleteGroup(group)
        }
    }
    fun deleteAllGroup(){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            repository2.deleteAllGroups()
        }
    }
    //    fun updateTask(task:Task){
//        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
//            repository.updateTask(task)
//        }
//    }
    fun searchGroup(searchkey:String):LiveData<List<Group>>{
        return repository2.getGroupsBySearchKey(searchkey).asLiveData()
    }








}