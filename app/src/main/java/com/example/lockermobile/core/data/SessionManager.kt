package com.example.lockermobile.core.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lockermobile.domain.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ROLE = stringPreferencesKey("user_role")
        private val USER_NAME = stringPreferencesKey("user_name")
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { it[USER_EMAIL] }
    val userRole: Flow<UserRole?> = context.dataStore.data.map { 
        it[USER_ROLE]?.let { role -> 
            try {
                UserRole.valueOf(role)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }

    suspend fun saveSession(email: String, role: UserRole, name: String) {
        context.dataStore.edit {
            it[USER_EMAIL] = email
            it[USER_ROLE] = role.name
            it[USER_NAME] = name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
