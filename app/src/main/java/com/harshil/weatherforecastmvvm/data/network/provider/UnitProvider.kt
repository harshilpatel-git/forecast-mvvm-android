package com.resocoder.forecastmvvm.data.provider

import com.harshil.weatherforecastmvvm.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}