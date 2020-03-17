package com.harshil.weatherforecastmvvm.data.network

import androidx.lifecycle.LiveData
import com.harshil.weatherforecastmvvm.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    // Suspend function because we want it to be called from a coroutine
    suspend fun fetchCurrentWeather(
        location: String,
        unit: String
    )
}