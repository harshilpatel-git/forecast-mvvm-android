package com.harshil.weatherforecastmvvm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harshil.weatherforecastmvvm.data.db.entity.CurrentWeatherEntry

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

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