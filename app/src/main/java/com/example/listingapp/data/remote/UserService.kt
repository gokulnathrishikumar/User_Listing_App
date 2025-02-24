package com.example.listingapp.data.remote

import com.example.listingapp.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("api/")
    suspend fun getUsers(@Query("results") results: Int): UserResponse
}
