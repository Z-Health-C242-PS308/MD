package com.sugara.z_health.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sugara.z_health.view.ui.home.HomeViewModel
import com.sugara.z_health.view.ui.journal.JournalViewModel
import com.sugara.z_health.view.ui.profile.ProfileViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(FormJournalViewModel::class.java)) {
            return FormJournalViewModel(mApplication) as T
        }else if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            return JournalViewModel(mApplication) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}