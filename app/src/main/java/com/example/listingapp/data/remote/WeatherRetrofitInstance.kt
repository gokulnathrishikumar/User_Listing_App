package com.example.listingapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofitInstance {

    val api: WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/") // Correct base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}
