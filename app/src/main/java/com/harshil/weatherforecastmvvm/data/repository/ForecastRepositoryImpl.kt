package com.harshil.weatherforecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.harshil.weatherforecastmvvm.data.db.CurrentWeatherDao
import com.harshil.weatherforecastmvvm.data.db.WeatherLocationDao
import com.harshil.weatherforecastmvvm.data.db.entity.WeatherLocation
import com.harshil.weatherforecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.harshil.weatherforecastmvvm.data.network.WeatherNetworkDataSource
import com.harshil.weatherforecastmvvm.data.network.response.CurrentWeatherResponse
import com.harshil.weatherforecastmvvm.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistedFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        // Dispatchers.IO- we can use massive amount of threads and destroy as and when needed
        // withContext returns a value
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    // Whenever there is a new weather or change in weather, we persist the update in the local database
    private fun persistedFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {

        // Lifecycle does not affect repository, so we can use global scope
        // Dispatchers.IO- we can use massive amount of threads and destroy as and when needed
        // launch returns a job
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    // When there is no stored weather data in database during first time app initialize
    private suspend fun initWeatherData() {
        // .value because getLocation() returns LiveData and we want its value
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        // Because it can be null when the app launches for the first time
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchedCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            "m"
        )
    }

    private fun isFetchedCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }
}