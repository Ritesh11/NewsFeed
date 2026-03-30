package com.ritesh.newsfeed.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val COUNTRY_CODE = stringPreferencesKey("country_code")
    }

    // Read the country code
    val countryCodeFlow: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.COUNTRY_CODE] ?: "us" // Default to "us"
        }

    // Save the country code
    suspend fun updateCountryCode(code: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COUNTRY_CODE] = code
        }
    }
}