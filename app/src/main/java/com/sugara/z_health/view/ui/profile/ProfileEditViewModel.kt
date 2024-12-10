package com.sugara.z_health.view.ui.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.EditProfileResponse
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.AndroidViewModel

class ProfileEditViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val userRepository: UserRepository = UserRepository.getInstance(userPreference)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response

    fun getSession(): LiveData<UserLogin> {
        return userRepository.getSession().asLiveData()
    }

    fun updateProfile(
        userId: String,
        token: String,
        fullName: String,
        email: String,
        password: String,
        profileImg: Uri?
    ) {
        _isLoading.value = true

        // Menggunakan getApplication() untuk mendapatkan context
        val context = getApplication<Application>().applicationContext

        // Konversi teks ke RequestBody
        val fullNameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        // Konversi Uri gambar ke MultipartBody.Part
        val imagePart = profileImg?.let { uri ->
            val file = Helper().getFileFromUri(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
        }

        val client = ApiConfig.getApiService().updateUserProfile(
            userId,
            "Bearer $token",
            fullNameBody,
            emailBody,
            passwordBody,
            imagePart
        )

        client.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = ResponseWrapper(true, "Profile updated successfully")
                } else {
                    _response.value = ResponseWrapper(false, "Update failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })
    }
}
