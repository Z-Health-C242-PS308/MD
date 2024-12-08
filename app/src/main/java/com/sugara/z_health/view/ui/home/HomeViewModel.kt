package com.sugara.z_health.view.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.JournalModel
import com.sugara.z_health.data.model.JournalWeekModel
import com.sugara.z_health.data.model.RegisterModel
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)

    private val _isLoadingJournal = MutableLiveData<Boolean>()
    val isLoadingJournal: LiveData<Boolean> = _isLoadingJournal

    private val _isLoadingWeekJournal = MutableLiveData<Boolean>()
    val isLoadingWeekJournal: LiveData<Boolean> = _isLoadingWeekJournal

    private val _latestJournal = MutableLiveData<JournalModel>()
    val latestJournal: LiveData<JournalModel> = _latestJournal

    private  val _latestWeekJournal = MutableLiveData<JournalWeekModel>()
    val latestWeekJournal: LiveData<JournalWeekModel> = _latestWeekJournal

    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }

    fun getLatestJournal(userId : String) {
        _isLoadingJournal.value = true
        val client = ApiConfig.getApiService().getLatestJournal(userId)
        client.enqueue(object : Callback<JournalModel> {
            override fun onResponse(
                call: Call<JournalModel>,
                response: Response<JournalModel>
            ) {
                _isLoadingJournal.value = false
                if (response.isSuccessful && response.code() == 200) {

                    _latestJournal.value = response.body()
                }else{
                    _latestJournal.value = null
                }
            }
            override fun onFailure(call: Call<JournalModel>, t: Throwable) {
                _isLoadingJournal.value = false
            }
        })
    }

    fun getLatestWeekJournal(userId : String) {
        _isLoadingWeekJournal.value = true
        val client = ApiConfig.getApiService().getLatestWeekJournal(userId)
        client.enqueue(object : Callback<JournalWeekModel> {
            override fun onResponse(
                call: Call<JournalWeekModel>,
                response: Response<JournalWeekModel>
            ) {

                _isLoadingWeekJournal.value = false
                if (response.isSuccessful && response.code() == 200) {

                    _latestWeekJournal.value = response.body()
                }else{
                    _latestWeekJournal.value = null
                }
            }
            override fun onFailure(call: Call<JournalWeekModel>, t: Throwable) {
                Log.d("HomeViewModel", "onFailure api: ${t.message}")
                _isLoadingWeekJournal.value = false
            }
        })
    }


}