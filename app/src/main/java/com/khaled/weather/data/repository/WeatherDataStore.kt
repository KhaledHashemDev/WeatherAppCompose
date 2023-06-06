package com.khaled.weather.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.khaled.weather.data.model.StoredPreferences
import com.khaled.weather.utils.TempUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherDataStore(context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("WEATHER")
    private var pref = context.datastore

    companion object {
        val lastSearchedLocation = stringPreferencesKey("LAST_SEARCHED_LOCATION")
        val preferredTempMeasure = stringPreferencesKey("PREFERRED_TEMP_UNIT")
    }

    suspend fun upToDateSearchedLocation(searchedLocation: String) {
        pref.edit {
            it[lastSearchedLocation] = searchedLocation
        }
    }

    suspend fun upToDatePreferredTempUnit(tempUnit: TempUnit) {
        pref.edit {
            it[preferredTempMeasure] = tempUnit.name
        }
    }

    fun getStoredPreferences(): Flow<StoredPreferences> =
        pref.data.map {
            StoredPreferences(
                it[lastSearchedLocation],
                it[preferredTempMeasure]?.let { t -> TempUnit.valueOf(t) } ?: TempUnit.Celcius,
            )
        }
}
