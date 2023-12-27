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
    val readAllTaskData: LiveData<List<Task>>
    val taskRepository:TaskRepository

    val readAllGroupData: LiveData<List<Group>>
    val groupRepository:GroupRepository
    init {
        val taskDAO= TaskRoomDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDAO)
        readAllTaskData = taskRepository.readAlldata

        val groupDAO= TaskRoomDatabase.getDatabase(application).groupDao()
        groupRepository = GroupRepository(groupDAO)
        readAllGroupData = groupRepository.readAlldata
    }


    fun addTask(task:Task){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            taskRepository.insertTask(task)
        }
    }
    fun addTasks(tasks: List<Task>){
        viewModelScope.launch(Dispatchers.IO) { // that code will be run in background thread, coroutine scope
            tasks.forEach{
                taskRepository.insertTask(it)
            }
        }
    }
    fun deleteTask(task:Task){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            taskRepository.deleteTask(task)
        }
    }
    fun deleteAllTask(){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            taskRepository.deleteAllTasks()
        }
    }
//    fun updateTask(task:Task){
//        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
//            repository.updateTask(task)
//        }
//    }
    fun searchTask(searchkey:String):LiveData<List<Task>>{
            return taskRepository.getTasksBySearchKey(searchkey).asLiveData()
    }

//Group

    fun addGroup(group:Group){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            groupRepository.insertGroup(group)
        }
    }
    fun addGroups(groups: List<Group>){
        viewModelScope.launch(Dispatchers.IO) { // that code will be run in background thread, coroutine scope
            groups.forEach{
                groupRepository.insertGroup(it)
            }
        }
    }
    fun deleteGroup(group: Group){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            groupRepository.deleteGroup(group)
        }
    }
    fun deleteAllGroup(){
        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
            groupRepository.deleteAllGroups()
        }
    }
    //    fun updateTask(task:Task){
//        viewModelScope.launch(Dispatchers.IO){ // that code will be run in background thread, coroutine scope
//            repository.updateTask(task)
//        }
//    }
    fun searchGroup(searchkey:String):LiveData<List<Group>>{
        return groupRepository.getGroupsBySearchKey(searchkey).asLiveData()
    }








}