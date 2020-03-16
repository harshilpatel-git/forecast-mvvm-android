package com.harshil.weatherforecastmvvm.data

import com.harshil.weatherforecastmvvm.data.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "367c0911b5c470d92f075a03d4acbdec"

//http://api.weatherstack.com/current?access_key=367c0911b5c470d92f075a03d4acbdec&query=New York

interface WeatherStackApiService {

    @GET("current")
    fun getCurrentWeather(
        // Specify the query parameter for request url mentioned above
        @Query("query") location: String
    ): Deferred<CurrentWeatherResponse> // Deferred- It is part of kotlin coroutines, used as we have to wait until we get response from api

    companion object {
        operator fun invoke(): WeatherStackApiService {
            // Creating an interceptor to add the common query parameter ie. access_key for all request made for the url
            val requestInterceptor = Interceptor { chain ->

                // Updating the url with the common query parameter
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()

                // Setting the updated url to the request
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                // Proceeding with the updated request
                return@Interceptor chain.proceed(request)
            }

            // Adding the interceptor to the client
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            // Returning the client with the base url
            return Retrofit.Builder()
                .client(okHttpClient)
                // Setting the base url for the request
                .baseUrl("http://api.weatherstack.com/")
                // As we are using Deferred<T> we need to specify call adapter factory for coroutines
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                // As we want response to be converted from JSON to object using GSON converter
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackApiService::class.java)
        }
    }
}