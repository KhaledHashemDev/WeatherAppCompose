package com.khaled.weather.data.model

import com.khaled.weather.utils.TempUnit

data class StoredPreferences(val lastStoredLocation: String? = null, val tempUnit: TempUnit = TempUnit.Celcius)
