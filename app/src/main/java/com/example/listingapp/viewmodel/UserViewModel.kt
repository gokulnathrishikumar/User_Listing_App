package com.example.listingapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.listingapp.data.local.UserEntity
import com.example.listingapp.data.local.toUserEntity
import com.example.listingapp.data.model.WeatherResponse
import com.example.listingapp.data.remote.UserRetrofitInstance
import com.example.listingapp.data.remote.WeatherRetrofitInstance
import com.example.listingapp.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Locale
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class UserViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val context: Context = getApplication<Application>().applicationContext

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    private val _cityName = MutableStateFlow<String?>(null)
    val cityName: StateFlow<String?> = _cityName

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
            .addOnSuccessListener { location: Location? ->5
                Log.e("TAG", "getCurrentLocationWeather: location : "+location )
                if (location != null) {
                    fetchWeather(location.latitude, location.longitude)
                    fetchUserLocation()
                } else {
                    fetchUserLocation()
                    Log.e("WeatherAPI", "Last known location is null")
                }
            }
            .addOnFailureListener {
                Log.e("WeatherAPI", "Failed to get location: ${it.message}")
            }
    }

    fun fetchUserLocation() {
        getUserLocation(context) { location ->
            viewModelScope.launch {
                if (location != null) {
                    val city = getCityName(context, location.latitude, location.longitude)
                    Log.e("TAG", "fetchUserLocation: city : $city")
                    _cityName.value = city
                } else {
                    Log.e("TAG", "fetchUserLocation: Location is null")
                }
            }
        }
    }


    fun getCityName(context: Context, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.get(0)?.locality ?: "Unknown City"
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown City"
        }
    }



    @SuppressLint("MissingPermission")
    fun getUserLocation(context: Context, onLocationReceived: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        Log.e("TAG", "fetchUserLocation: city 1: ")

        // Try Last Known Location First
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReceived(location)
                } else {
                    // If last location is null, request fresh location update
                    requestFreshLocation(context, fusedLocationClient, onLocationReceived)
                }
            }
            .addOnFailureListener {
                onLocationReceived(null)
            }
    }

    // Function to Request a Fresh Location Update
    @SuppressLint("MissingPermission")
    private fun requestFreshLocation(
        context: Context,
        fusedLocationClient: FusedLocationProviderClient,
        onLocationReceived: (Location?) -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).setWaitForAccurateLocation(true).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)
                onLocationReceived(result.lastLocation)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
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
