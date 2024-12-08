package com.sugara.z_health.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.LoginModel
import com.sugara.z_health.data.model.RegisterModel
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response

    fun login(user: UserLogin) {
        _isLoading.value = true
        Log.d("LoginViewModel", "login: ${user}")
        val client = ApiConfig.getApiService().login(
            user.email ?: "",
            user.password ?: ""
        )
        client.enqueue(object : Callback<LoginModel> {
            override fun onResponse(
                call: Call<LoginModel>,
                response: Response<LoginModel>
            ) {
                _isLoading.value = false
                Log.d("LoginViewModel", "onResponse: ${response}")
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
                            token = body?.token,
                            isLogin = true
                        )
                        mUserRepository.saveSession(userLogin)
                    }
                } else if (response.code() == 404) {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("LoginViewModel", "onResponse Error: $errorResponse")
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, LoginModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
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