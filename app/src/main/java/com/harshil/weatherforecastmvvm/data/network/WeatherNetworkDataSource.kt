package com.harshil.weatherforecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.harshil.weatherforecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        unit: String
    )
}