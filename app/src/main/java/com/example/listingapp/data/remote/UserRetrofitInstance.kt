package com.example.listingapp.data.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitInstance {
    private const val BASE_URL = "https://randomuser.me/"

    val api: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}
