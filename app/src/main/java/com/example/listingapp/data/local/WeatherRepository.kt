package com.example.listingapp.data.local

import com.example.listingapp.data.model.WeatherResponse
import com.example.listingapp.data.remote.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {
    suspend fun getWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        return weatherService.getWeather(lat, lon, apiKey)
    }
}
