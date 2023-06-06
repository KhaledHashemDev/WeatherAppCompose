package com.khaled.weather.data.model


import com.google.gson.annotations.SerializedName

data class CityWeatherModel(
    @SerializedName("base")
    val base: String? = "",
    @SerializedName("clouds")
    val clouds: CloudsModel? = CloudsModel(),
    @SerializedName("cod")
    val cod: Int? = 0,
    @SerializedName("coord")
    val coord: CoordinatesModel? = CoordinatesModel(),
    @SerializedName("dt")
    val dt: Int? = 0,
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("main")
    val main: MainWeatherModel? = MainWeatherModel(),
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("sys")
    val sys: SysModel? = SysModel(),
    @SerializedName("timezone")
    val timezone: Int? = 0,
    @SerializedName("visibility")
    val visibility: Int? = 0,
    @SerializedName("weather")
    val weather: List<WeatherModel?>? = listOf(),
    @SerializedName("wind")
    val wind: WindModel? = WindModel()
)