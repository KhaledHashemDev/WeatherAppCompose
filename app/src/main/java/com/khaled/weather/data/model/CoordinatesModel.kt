package com.khaled.weather.data.model


import com.google.gson.annotations.SerializedName

data class CoordinatesModel(
    @SerializedName("lat")
    val lat: Double? = 0.0,
    @SerializedName("lon")
    val lon: Double? = 0.0
)