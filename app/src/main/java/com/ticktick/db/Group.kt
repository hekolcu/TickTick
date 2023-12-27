package com.ticktick.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ticktick.util.Constants

@Entity(tableName = Constants.TABLENAME_GROUP)
class Group(
    var title:String,
    var userId:Int,
    @PrimaryKey
    var groupId:Int
) {
    override fun toString(): String {
        return "title$title\nuser_id=$userId\ngroup_id=$groupId"
    }
}