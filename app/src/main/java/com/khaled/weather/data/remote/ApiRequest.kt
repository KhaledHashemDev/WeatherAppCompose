package com.khaled.weather.data.remote

import com.khaled.weather.data.model.CityWeatherModel
import com.khaled.weather.data.remote.ApiDetails.KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET(ApiDetails.URL_WEATHER)
    suspend fun getWeather(@Query("q") searchParam: String, @Query("appid") appId: String = KEY): CityWeatherModel

    @GET(ApiDetails.URL_WEATHER)
    suspend fun getWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") appId: String = KEY): CityWeatherModel
}
