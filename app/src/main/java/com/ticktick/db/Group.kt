package com.ticktick.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ticktick.util.Constants
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.TABLENAME_GROUP)
data class Group(
    var title: String = "",
    var userId: Int = 0
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var groupId: Int = 0

    override fun toString(): String {
        return title
    }
}