package com.example.listingapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,   // ✅ Added Wind Data
    val rain: Rain?,  // ✅ Added Rain Data (Nullable)
    val name: String
)

data class Main(
    val temp: Double,
    val humidity: Int // ✅ Added Humidity
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double // ✅ Wind Speed in km/h
)

data class Rain(
    @SerializedName("1h") val h1h: Double? // ✅ Rainfall in mm (Last 1 Hour)
)
