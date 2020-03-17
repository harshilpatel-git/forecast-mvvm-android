package com.harshil.weatherforecastmvvm

import android.app.Application
import com.harshil.weatherforecastmvvm.data.db.CurrentWeatherDao
import com.harshil.weatherforecastmvvm.data.db.ForecastDatabase
import com.harshil.weatherforecastmvvm.data.network.*
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepository
import com.harshil.weatherforecastmvvm.data.repository.ForecastRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
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

        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        // Binding the connectivity interface, as it is an interface its binding is different that above binding
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        bind() from singleton { WeatherStackApiService(instance()) }

        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance<WeatherStackApiService>()) }

        // For interface binding we need to provide type<> for the bind() function and bind using with keyword
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(
                // we can also directly use instance()
                // instance is provided for line 27- bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
                instance<CurrentWeatherDao>(),
                // instance is provided for line 36 bind<WeatherNetworkDataSource>()
                instance<WeatherNetworkDataSource>()
            )
        }
    }


}