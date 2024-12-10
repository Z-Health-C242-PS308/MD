package com.sugara.z_health.view.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sugara.z_health.data.UserRepository
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import com.sugara.z_health.data.pref.dataStore
import kotlinx.coroutines.launch

class ProfileViewModel (application: Application) : ViewModel(){
    private val userPreference = UserPreference.getInstance(application.dataStore)
    private val mUserRepository: UserRepository = UserRepository.getInstance(userPreference)
    fun logout(){
        viewModelScope.launch {
            mUserRepository.logout()
        }
    }

    fun getSession(): LiveData<UserLogin> {
        return mUserRepository.getSession().asLiveData()
    }
}