package com.khaled.weather.data.remote

object ApiDetails {

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val URL_WEATHER = "weather"
    const val KEY = "732a02e05e17242f2b2bfba88ac1a9bc"
    private const val ICON_WEATHER = "https://openweathermap.org/img/wn/{icon_id}@2x.png"
    fun iconUrl(icon: String) = ICON_WEATHER.replace("{icon_id}", icon)
}
