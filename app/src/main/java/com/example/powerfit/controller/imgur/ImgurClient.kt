package com.example.powerfit.controller.imgur

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImgurClient {
    private const val BASE_URL = "https://api.imgur.com/"

    val api: ImgurApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgurApi::class.java)
    }
}