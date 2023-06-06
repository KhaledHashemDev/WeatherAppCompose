package com.khaled.weather.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khaled.weather.data.model.CityWeatherModel
import com.khaled.weather.data.model.GeoLocationModel
import com.khaled.weather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repository: Repository,
) : ViewModel() {

    private val _weatherDetails = MutableStateFlow(CityWeatherModel())
    val weatherDetails: StateFlow<CityWeatherModel> = _weatherDetails

    private val _currentLocationDetails = MutableStateFlow(CityWeatherModel())
    val currentLocationDetails: StateFlow<CityWeatherModel> = _currentLocationDetails

    private val _reportError: MutableStateFlow<String> = MutableStateFlow("")
    val reportError: StateFlow<String> = _reportError

    fun fetchWeatherDetails(searchCity: String, onSuccess: suspend (String) -> Unit) {
        viewModelScope.launch {
            try {
                _weatherDetails.value = repository.getWeather(searchCity)
                onSuccess(searchCity)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _reportError.value = "Unable to fetch details for $searchCity: ${e.message}"
            }
        }
    }

    fun fetchWeatherDetails(geoLocationModel: GeoLocationModel) {
        geoLocationModel.lat?.let { lat ->
            geoLocationModel.lon?.let { lon ->
                viewModelScope.launch {
                    try {
                        _currentLocationDetails.value = repository.getWeather(lat, lon)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        _reportError.value = "Unable to fetch details for $geoLocationModel: ${e.message}"
                    }
                }
            }
        }
    }

    fun clearError() {
        _reportError.value = ""
    }
}


