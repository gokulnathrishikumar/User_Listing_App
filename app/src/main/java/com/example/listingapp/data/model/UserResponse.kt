package com.example.listingapp.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val results: List<UserData>
)

data class UserData(
    val name: Name,
    val email: String,
    val location: Location,  // `coordinates` should be inside `Location`
    val picture: Picture
)

data class Name(
    val first: String,
    val last: String
)

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates  // Correctly nested inside `Location`
)

data class Picture(
    val large: String
)

data class Coordinates(
    @SerializedName("latitude") val latitude: String,   // API returns String
    @SerializedName("longitude") val longitude: String  // API returns String
)
