package com.ticktick.util

import com.ticktick.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    fun getUser(): Call<User>
}