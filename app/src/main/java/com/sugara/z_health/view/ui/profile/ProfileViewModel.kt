package com.sugara.z_health.view.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.LoginModel
import com.sugara.z_health.data.model.ProfileModel
import com.sugara.z_health.data.model.RegisterModel
import com.sugara.z_health.data.model.User
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel (application: Application) : AndroidViewModel(application){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response
    val context = getApplication<Application>().applicationContext

    fun logout(){
        viewModelScope.launch {
            mUserRepository.logout()
        }
    }

    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }

    fun update(user : ProfileModel) {
        _isLoading.value = true
        val fullnameBody = user.fullname?.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = user.email?.toRequestBody("text/plain".toMediaTypeOrNull())
        val birthdateBody = user.birthdate?.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = user.password?.toRequestBody("text/plain".toMediaTypeOrNull())
        val confirmPassBody = user.confirmPass?.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = user.profileImg?.let { uri ->
            val file = Helper().uriToFile(uri,context)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profile_img", file.name, requestFile)
        }


        val client = ApiConfig.getApiService().updateProfile(
            fullnameBody ?: return,
            emailBody ?: return,
            birthdateBody ?: return,
            imagePart,
            passwordBody ?: return,
            confirmPassBody ?: return,
            "Bearer ${user.token}" ?:"",
            user.userId ?:""
        )
        client.enqueue(object : Callback<LoginModel> {
            override fun onResponse(
                call: Call<LoginModel>,
                response: Response<LoginModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.code() == 200) {
                    _response.value = ResponseWrapper(true, response.body()?.message ?: "Unknown success message")
                    viewModelScope.launch {
                        val body = response.body()?.user
                        val userLogin = UserLogin(
                            userId = body?.userId,
                            fullname = body?.fullname,
                            email = body?.email,
                            birthdate = body?.birthdate,
                            profileImg = body?.profileImg,
                            token = user?.token,
                            isLogin = true
                        )
                        mUserRepository.saveSession(userLogin)
                    }
                } else if (response.code() == 400) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, LoginModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                }else if (response.code() == 403) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, LoginModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
                    Log.d("ProfileViewModel", "onResponse: ${response.message()}")
                    _response.value = ResponseWrapper(false, "Unexpected error")
                }
            }
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })


    }
}