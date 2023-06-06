package com.khaled.weather.data.repository

import com.khaled.weather.data.model.CityWeatherModel

interface Repository {

    suspend fun getWeather(inputString: String): CityWeatherModel

    suspend fun getWeather(latitude: Double, longitude: Double): CityWeatherModel
}
