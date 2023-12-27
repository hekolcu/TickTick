package com.ticktick.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ticktick.util.Constants

@Entity(tableName = Constants.TABLENAME)
class Task(
    var name:String,
    var desc:String,
    var date:String,
    @PrimaryKey
    var id:Int=0
) {
    override fun toString(): String {
        return "Task name='$name',date=$date, desc='$desc', id=$id)"
    }
}