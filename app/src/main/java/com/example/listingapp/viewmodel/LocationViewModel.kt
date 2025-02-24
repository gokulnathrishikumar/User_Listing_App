package com.example.listingapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listingapp.ui.getCityName
import com.example.listingapp.ui.getUserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    @ApplicationContext private val context: Context // Inject Context properly
) : ViewModel() {

    private val _cityName = MutableStateFlow<String?>(null)
    val cityName: StateFlow<String?> = _cityName

    init {
        fetchUserLocation()
    }

    private fun fetchUserLocation() {
        viewModelScope.launch {
            getUserLocation(context) { location ->
                location?.let {
                    val city = getCityName(context, it.latitude, it.longitude)
                    _cityName.value = city
                }
            }
        }
    }
}
