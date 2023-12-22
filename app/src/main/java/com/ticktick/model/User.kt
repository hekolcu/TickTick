package com.ticktick.model

import com.google.gson.annotations.SerializedName

class User (
    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("username")
    val user_name: String = "",
    @SerializedName("profile_photo")
    val profile_photo: String = "")
{
    override fun toString(): String {
        return "User\nName: $user_name"
    }
}