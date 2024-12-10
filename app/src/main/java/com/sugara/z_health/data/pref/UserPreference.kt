package com.sugara.z_health.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sugara.z_health.data.model.UserLogin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to access the DataStore instance.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    // Save user session data in the DataStore.
    suspend fun saveSession(user: UserLogin) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId ?: ""
            preferences[TOKEN_KEY] = user.token ?: ""
            preferences[IS_LOGIN_KEY] = user.isLogin ?: false
            preferences[FULL_NAME_KEY] = user.fullname ?: ""
            preferences[EMAIL_KEY] = user.email ?: ""
            preferences[PROFILE_IMG_KEY] = user.profileImg ?: ""
        }
    }

    // Retrieve user session data as a Flow.
    fun getSession(): Flow<UserLogin> {
        return dataStore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY] ?: ""
            val token = preferences[TOKEN_KEY] ?: ""
            val isLogin = preferences[IS_LOGIN_KEY] ?: false
            val name = preferences[FULL_NAME_KEY] ?: ""
            val email = preferences[EMAIL_KEY] ?: ""
            val profileImg = preferences[PROFILE_IMG_KEY] ?: ""
            UserLogin(
                userId = userId,
                token = token,
                isLogin = isLogin,
                fullname = name,
                email = email,
                profileImg = profileImg
            )
        }
    }

    // Logout by clearing the session data from the DataStore.
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.remove(IS_LOGIN_KEY)
            preferences.remove(FULL_NAME_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(PROFILE_IMG_KEY)
        }
    }




    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        // Keys to store and retrieve data from DataStore.
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val FULL_NAME_KEY = stringPreferencesKey("fullname")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PROFILE_IMG_KEY = stringPreferencesKey("profile_img")

        // Get the Singleton instance of UserPreference.
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
