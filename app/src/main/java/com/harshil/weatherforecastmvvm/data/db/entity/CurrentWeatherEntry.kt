package com.harshil.weatherforecastmvvm.data.response


import com.google.gson.annotations.SerializedName

data class CurrentWeatherEntry(
    @SerializedName("cloudcover")
    val cloudcover: Int,
    @SerializedName("feelslike")
    val feelslike: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("observation_time")
    val observationTime: String,
    @SerializedName("precip")
    val precip: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("temparature")
    val temparature: Int,
    @SerializedName("uv_index")
    val uvIndex: Int,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_degree")
    val windDegree: Int,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Int
)