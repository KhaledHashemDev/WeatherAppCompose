package com.khaled.weather.data.repository

import com.khaled.weather.data.model.CityWeatherModel
import com.khaled.weather.data.remote.ApiRequest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiRequest: ApiRequest,
) : Repository {

    override suspend fun getWeather(inputString: String): CityWeatherModel {
        return apiRequest.getWeather(inputString)
    }

    override suspend fun getWeather(latitude: Double, longitude: Double): CityWeatherModel {
        return apiRequest.getWeather(latitude, longitude)
    }
}
