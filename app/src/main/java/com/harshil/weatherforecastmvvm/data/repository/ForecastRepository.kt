package com.harshil.weatherforecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.harshil.weatherforecastmvvm.data.db.entity.WeatherLocation
import com.harshil.weatherforecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    // out-  so that we can return also the classes implementing the UnitSpecificCurrentWeatherEntry interface
    // and not only UnitSpecificCurrentWeatherEntry interface
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}