package com.harshil.weatherforecastmvvm.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.harshil.weatherforecastmvvm.R
import com.resocoder.forecastmvvm.ui.base.ScopedFragment
import com.resocoder.forecastmvvm.ui.weather.current.CurrentWeatherViewModel
import com.resocoder.forecastmvvm.ui.weather.current.CurrentWeatherViewModelFactory
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUi()
    }

    // launch we are using from the ScopedFragment that was created for local scope
    // Here we can also use GlobalScope.launch but is is not a good practise to use global scope on
    // lifecycle dependent classes
    private fun bindUi() = launch {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.name)
        })
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            // Initially the app will crash here, to avoid we have added the below line as we are
            // fetching data from the database which will be updated when there is change from the
            // api, so initially there will be no data in the database and currentWeather will be null
            // It will be updated by itself once data is available from api
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelsLikeTemperature)
//            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
//            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

//            GlideApp.with(this@CurrentWeatherFragment)
//                .load("http:${it.conditionIconUrl}")
//                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
        textView_feels_like_temperature.text = "$feelsLike$unitAbbreviation"
    }


    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "$precipitationVolume$unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

}
