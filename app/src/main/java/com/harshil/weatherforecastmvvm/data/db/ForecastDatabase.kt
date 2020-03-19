package com.harshil.weatherforecastmvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harshil.weatherforecastmvvm.data.db.entity.CurrentWeatherEntry
import com.harshil.weatherforecastmvvm.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeatherEntry::class, WeatherLocation::class],
    version = 1
)
// To convert list type column entry to string and vice versa, for eg. weather_descriptions and weather_icons in CurrentWeatherEntry object
@TypeConverters(StringListConverter::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {

        // Volatile-  All the threads will have immediate access
        @Volatile
        private var instance: ForecastDatabase? = null

        // LOCK is need for using threads, basically it is a dummy object to make sure no two threads are currently doing the same thing
        private val LOCK = Any()


        // Here we want to first check if instance is not null- return instance else if it is null we want to call a synchronized block of code
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            // Again check if instance is initialized- return instance, else build database
            // also whatever buildDatabase returns we want to set it to instance
            instance ?: builtDatabase(context).also { instance = it }
        }

        private fun builtDatabase(context: Context) =
            // Here we want the whole application context, weather we are calling it from a fragment or an activity
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java, "forecast.db"
            )
                .build()
    }

}