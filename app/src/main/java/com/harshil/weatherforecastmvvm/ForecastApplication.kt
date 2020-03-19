package com.harshil.weatherforecastmvvm

import android.app.Application
import com.harshil.weatherforecastmvvm.data.db.CurrentWeatherDao
import com.harshil.weatherforecastmvvm.data.db.ForecastDatabase
import com.harshil.weatherforecastmvvm.data.network.*
import com.harshil.weatherforecastmvvm.data.provider.LocationProvider
import com.harshil.weatherforecastmvvm.data.provider.LocationProviderImpl
import com.harshil.weatherforecastmvvm.data.provider.UnitProvider
import com.harshil.weatherforecastmvvm.data.provider.UnitProviderImpl
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepository
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepositoryImpl
import com.jakewharton.threetenabp.AndroidThreeTen
import com.resocoder.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {

        // It provided androidx regarding classes ready to use
        import(androidXModule(this@ForecastApplication))

        // instance()- it is instance of context ready to use received from above androidxModule import
        bind() from singleton { ForecastDatabase(instance()) }

        // instance<ForecastDatabase>-  will get the instance for ForecastDatabase already created in above statement
        // initializing currentWeatherDao
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }

        // initializing weatherLocationDao
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }


        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        // Binding the connectivity interface, as it is an interface its binding is different that above binding
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        bind() from singleton { WeatherStackApiService(instance()) }

        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance<WeatherStackApiService>()) }


        bind<LocationProvider>() with singleton { LocationProviderImpl() }

        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                // we can also directly use instance()
                instance<CurrentWeatherDao>(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }

        // For binding class or a factory
        // instance() is instance<ForecastRepository>() and instance<UnitProvider>() respectively
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        // Used for ZonedDateTime
        AndroidThreeTen.init(this)
    }


}