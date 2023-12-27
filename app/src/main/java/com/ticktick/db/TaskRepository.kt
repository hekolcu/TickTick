package com.ticktick.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDAO: TaskDAO) {
    val readAlldata:LiveData<List<Task>> = taskDAO.getAllTasks()

    fun insertTask(task:Task){
        taskDAO.insertTask(task)
    }
    fun insertTasks(tasks:ArrayList<Task>){
        taskDAO.insertAllTask(tasks)
    }

    fun deleteTask(task:Task){
        taskDAO.deleteTask(task)
    }

    fun deleteAllTasks(){
        taskDAO.deleteAllTasks()
    }
    fun getAllTasks():LiveData<List<Task>>{
        return taskDAO.getAllTasks()
    }

    fun getTaskById(id:Int):Task{
        return taskDAO.getTaskById(id)
    }

    fun getTasksBySearchKey(searchKey:String): Flow<List<Task>> {
        return taskDAO.getTasksBySearchKey(searchKey)
    }
}