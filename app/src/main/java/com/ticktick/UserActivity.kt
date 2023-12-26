package com.ticktick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ticktick.backgroundservice.CustomWorker
import com.ticktick.databinding.ActivityUserBinding
import com.ticktick.model.User
import com.ticktick.retrofit.ApiClient
import com.ticktick.util.ApiService

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.getClient().create(ApiService::class.java)

        val workManager = WorkManager.getInstance(applicationContext)

        val request = OneTimeWorkRequest.Builder(CustomWorker::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(request)

        workManager.getWorkInfoByIdLiveData(request.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val jsonUser = workInfo.outputData.getString("json_user")
                    if (jsonUser != null) {
                        val user = Gson().fromJson(jsonUser, User::class.java)
                        updateUserUI(user)
                    }
                }
            }
    }

    private fun updateUserUI(user: User) {
        binding.username.text = user.user_name

        val profilePhotoUrl = user.profile_photo
        Picasso.get()
            .load(profilePhotoUrl)
            .resize(700, 700)
            .centerCrop()
            .error(R.drawable.ic_launcher_background)
            .into(binding.profilePhoto)
    }

}
