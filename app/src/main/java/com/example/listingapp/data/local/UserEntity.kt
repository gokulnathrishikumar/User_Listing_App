package com.example.listingapp.data.local

import com.example.listingapp.data.model.UserData

data class UserEntity(
    val id: String,
    val name: String,
    val profileImage: String,
    val email: String,
    val location: String,
    val latitude: Double,  // Add latitude
    val longitude: Double  // Add longitude
)

// Extension function to map API response to UserEntity
fun UserData.toUserEntity(): UserEntity {
    return UserEntity(
        id = email, // Using email as a unique ID
        name = "${name.first} ${name.last}",
        profileImage = picture.large,
        email = email,
        location = "${location.city}, ${location.country}",
        latitude = location.coordinates.latitude.toDoubleOrNull() ?: 0.0, // Extract latitude
        longitude = location.coordinates.longitude.toDoubleOrNull() ?: 0.0 // Extract longitude

    )
}
