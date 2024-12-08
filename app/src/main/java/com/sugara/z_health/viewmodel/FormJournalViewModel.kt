package com.sugara.z_health.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.Journal
import com.sugara.z_health.data.model.JournalModel
import com.sugara.z_health.data.model.RegisterModel
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import com.sugara.z_health.network.ApiConfig
import com.sugara.z_health.viewmodel.wrapper.ResponseWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormJournalViewModel(application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)
    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response

    fun insertJournal(journal : Journal) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().insertJournal(
            user_id = journal.userId ?: "",
            waktu_belajar = journal.waktuBelajar ?: 0.0,
            waktu_belajar_tambahan = journal.waktuBelajarTambahan ?: 0.0,
            waktu_tidur = journal.waktuTidur ?: 0.0,
            aktivitas_fisik = journal.aktivitasFisik ?: 0.0,
            aktivitas_sosial = journal.aktivitasSosial ?: 0.0,
            jurnal_harian = journal.jurnalHarian ?: ""
        )
        client.enqueue(object : Callback<JournalModel> {
            override fun onResponse(
                call: Call<JournalModel>,
                response: Response<JournalModel>
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
            override fun onFailure(call: Call<JournalModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })


    }

}