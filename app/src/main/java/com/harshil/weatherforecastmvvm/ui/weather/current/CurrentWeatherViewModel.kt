package com.resocoder.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepository
import com.harshil.weatherforecastmvvm.internal.UnitSystem
import com.harshil.weatherforecastmvvm.internal.lazyDeferred
import com.resocoder.forecastmvvm.data.provider.UnitProvider

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val unitSystem = UnitSystem.METRIC

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
