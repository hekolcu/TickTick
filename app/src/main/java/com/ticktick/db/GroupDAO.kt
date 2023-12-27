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
interface GroupDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group:Group)

    @Delete
    fun deleteGroup(group:Group)

    @Query("DELETE FROM ${Constants.TABLENAME_GROUP}")
    fun deleteAllGroups()


    @Query("SELECT * FROM ${Constants.TABLENAME_GROUP} ORDER BY groupId ASC")
    fun getAllGroups(): LiveData<List<Group>>

    @Query("SELECT * FROM ${Constants.TABLENAME_GROUP} WHERE groupId =:id")
    fun getGroupById(id:Int):Group

    @Query("SELECT * FROM ${Constants.TABLENAME_GROUP} WHERE title LIKE :searchKey")
    fun getGroupsBySearchKey(searchKey:String): Flow<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGroup(tasks: ArrayList<Group>){
        tasks.forEach{
            insertGroup(it)
        }
    }
}