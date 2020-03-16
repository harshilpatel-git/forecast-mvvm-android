package com.harshil.weatherforecastmvvm.data.network.response


import com.google.gson.annotations.SerializedName
import com.harshil.weatherforecastmvvm.data.db.entity.CurrentWeatherEntry
import com.harshil.weatherforecastmvvm.data.db.entity.Location

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    @SerializedName("location")
    val location: Location
)