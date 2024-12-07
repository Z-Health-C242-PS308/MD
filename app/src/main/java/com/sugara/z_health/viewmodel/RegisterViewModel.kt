package com.sugara.z_health.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sugara.z_health.data.model.RegisterModel
import com.sugara.z_health.data.model.User
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (application: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response


    fun register(user : User) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(
            user.fullname ?:"",
            user.email ?:"",
            user.birthdate ?:"",
            user.profileImg ?:"",
            user.password ?:"",
            user.confirmPass ?:""
        )
        client.enqueue(object : Callback<RegisterModel> {
            override fun onResponse(
                call: Call<RegisterModel>,
                response: Response<RegisterModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.code() == 200) {
                    _response.value = ResponseWrapper(true, response.body()?.message ?: "Unknown success message")
                } else if (response.code() == 400) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, RegisterModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
                    _response.value = ResponseWrapper(false, "Unexpected error")
                }
            }
            override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })


    }
}