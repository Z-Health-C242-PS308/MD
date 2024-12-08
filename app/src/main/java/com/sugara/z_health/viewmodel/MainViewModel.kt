package com.sugara.z_health.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore

class MainViewModel(application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)
    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }

}