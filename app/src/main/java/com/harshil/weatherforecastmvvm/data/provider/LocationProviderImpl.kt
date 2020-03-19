package com.harshil.weatherforecastmvvm.data.provider

import com.harshil.weatherforecastmvvm.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Los Angeles"
    }
}