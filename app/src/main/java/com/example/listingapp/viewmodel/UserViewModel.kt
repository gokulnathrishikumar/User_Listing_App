package com.example.listingapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listingapp.data.local.UserEntity
import com.example.listingapp.data.local.toUserEntity
import com.example.listingapp.data.model.WeatherResponse
import com.example.listingapp.data.remote.UserRetrofitInstance
import com.example.listingapp.data.remote.WeatherRetrofitInstance
import com.example.listingapp.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    private var currentPage = 1
    private val pageSize = 26

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = UserRetrofitInstance.api.getUsers(pageSize)
                val newUsers = response.results.map { it.toUserEntity() }

                _users.value = _users.value + newUsers

                currentPage++
            } catch (e: Exception) {
                Log.e("UserAPI", "Error fetching users: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocationWeather() {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    fetchWeather(location.latitude, location.longitude)
                } else {
                    Log.e("WeatherAPI", "Last known location is null")
                }
            }
            .addOnFailureListener {
                Log.e("WeatherAPI", "Failed to get location: ${it.message}")
            }
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val response = WeatherRetrofitInstance.api.getWeather(latitude, longitude, Constants.WEATHER_API_KEY)
                _weather.value = response
            } catch (e: HttpException) {
                Log.e("WeatherAPI", "HTTP ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.e("WeatherAPI", "Exception: ${e.message}")
            }
        }
    }

    fun getWeatherForUser(user: UserEntity) {
        viewModelScope.launch {
            try {
                val lat = user.latitude
                val lon = user.longitude
                if (lat == 0.0 || lon == 0.0) {
                    Log.e("WeatherAPI", "Invalid coordinates for user ${user.id}")
                    return@launch
                }

                val response = WeatherRetrofitInstance.api.getWeather(lat, lon, Constants.WEATHER_API_KEY)
                _weather.value = response
            } catch (e: HttpException) {
                Log.e("WeatherAPI", "HTTP ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.e("WeatherAPI", "Exception: ${e.message}")
            }
        }
    }
}
