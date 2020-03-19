package com.harshil.weatherforecastmvvm.data.provider

import com.harshil.weatherforecastmvvm.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}