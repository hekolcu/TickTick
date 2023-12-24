package com.ticktick.backgroundservice

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.ticktick.model.User
import com.ticktick.retrofit.ApiClient
import com.ticktick.util.ApiService
import retrofit2.awaitResponse

class CustomWorker(var context: Context, var workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val apiService: ApiService = ApiClient.getClient().create(ApiService::class.java)
            val request = apiService.getUser()
            val response = request.awaitResponse()

            return if (response.isSuccessful) {
                val user = processUserResponse(response.body())
                Log.d("USER", "User: $user")

                if (user != null) {
                    val jsonUser = Gson().toJson(user)
                    val outputData = workDataOf("json_user" to jsonUser)
                    Result.success(outputData)
                } else {
                    Result.failure()
                }
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun processUserResponse(user: User?): User? {
        return user
    }
}
