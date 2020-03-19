package com.resocoder.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.harshil.weatherforecastmvvm.data.provider.UnitProvider
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepository
import com.harshil.weatherforecastmvvm.internal.UnitSystem
import com.harshil.weatherforecastmvvm.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
