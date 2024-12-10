package com.sugara.z_health.data


import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserLogin) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserLogin> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }




    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}