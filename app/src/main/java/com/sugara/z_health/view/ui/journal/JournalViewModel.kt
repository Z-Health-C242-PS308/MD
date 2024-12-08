package com.sugara.z_health.view.ui.journal

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.JournalAllModel
import com.sugara.z_health.data.model.JournalsItem
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalViewModel(application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)

    private val _listJournals = MutableLiveData<List<JournalsItem>>()
    val listJournals: LiveData<List<JournalsItem>> = _listJournals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }


    fun getJournals(userId : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllJournal(userId)
        client.enqueue(object : Callback<JournalAllModel> {
            override fun onResponse(
                call: Call<JournalAllModel>,
                response: Response<JournalAllModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listJournals.value = responseBody.journals as List<JournalsItem>
                    }
                }
            }
            override fun onFailure(call: Call<JournalAllModel>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}