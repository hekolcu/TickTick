package com.ticktick.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ticktick.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task:Task)

    @Delete
    fun deleteTask(task:Task)

    @Query("DELETE FROM ${Constants.TABLENAME}")
    fun deleteAllTasks()


    @Query("SELECT * FROM ${Constants.TABLENAME}")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM ${Constants.TABLENAME} WHERE id = :id")
    fun getTaskById(id:Int):Task

    @Query("SELECT * FROM ${Constants.TABLENAME} WHERE name LIKE :searchKey")
    fun getTasksBySearchKey(searchKey:String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTask(tasks: ArrayList<Task>){
        tasks.forEach{
            insertTask(it)
        }
    }
}