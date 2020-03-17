package com.resocoder.forecastmvvm.data.db.unitlocalized.current

import androidx.room.ColumnInfo
import com.harshil.weatherforecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry


data class MetricCurrentWeatherEntry(
    @ColumnInfo(name = "temparature")
    override val temperature: Double,
//    @ColumnInfo(name = "weather_descriptions")
//    override val conditionText: String,
//    @ColumnInfo(name = "weather_icons")
//    override val conditionIconUrl: String,
//    @ColumnInfo(name = "wind_speed")
//    override val windSpeed: Double,
//    @ColumnInfo(name = "wind_dir")
//    override val windDirection: String,
    @ColumnInfo(name = "precip")
    override val precipitationVolume: Double,
    @ColumnInfo(name = "feelslike")
    override val feelsLikeTemperature: Double,
    @ColumnInfo(name = "cloudcover")
    override val visibilityDistance: Double
) : UnitSpecificCurrentWeatherEntry