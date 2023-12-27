package com.ticktick.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val groupDAO:GroupDAO) {
    val readAlldata: LiveData<List<Group>> = groupDAO.getAllGroups()

    fun insertGroup(group:Group){
        groupDAO.insertGroup(group)
    }
    fun insertGroups(groups:ArrayList<Group>){
        groupDAO.insertAllGroup(groups)
    }

    fun deleteGroup(group:Group){
        groupDAO.deleteGroup(group)
    }

    fun deleteAllGroups(){
        groupDAO.deleteAllGroups()
    }
    fun getAllGroups(): LiveData<List<Group>> {
        return groupDAO.getAllGroups()
    }

    fun getGroupById(id:Int):Group{
        return groupDAO.getGroupById(id)
    }

    fun getGroupsBySearchKey(searchKey:String): Flow<List<Group>> {
        return groupDAO.getGroupsBySearchKey(searchKey)
    }
}