package com.ticktick.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ticktick.util.Constants

@Entity(tableName = Constants.TABLENAME_GROUP)
class Group(
    var title:String,
    var user_id:Int,
    @PrimaryKey
    var group_id:Int
) {
    override fun toString(): String {
        return "title$title\nuser_id=$user_id\ngroup_id=$group_id"
    }
}