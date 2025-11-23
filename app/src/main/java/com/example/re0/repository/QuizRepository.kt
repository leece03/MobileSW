package com.example.re0.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.re0.di.QuizPreferences
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@Singleton
class QuizRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val selectedState: Flow<Boolean> = dataStore.data
        .map { it[QuizPreferences.KEY_SELECTED] ?: false }

    val lastDate: Flow<String> = dataStore.data
        .map { it[QuizPreferences.KEY_LAST_DATE] ?: "" }

    suspend fun updateSelectedState(state: Boolean) {
        dataStore.edit {
            it[QuizPreferences.KEY_SELECTED] = state
        }
    }

    suspend fun updateLastDate(date: String) {
        dataStore.edit {
            it[QuizPreferences.KEY_LAST_DATE] = date
        }
    }
}
