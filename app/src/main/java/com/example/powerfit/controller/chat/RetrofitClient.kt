package com.example.powerfit.controller.chat

import com.example.powerfit.controller.Env
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", Env.apiKey)
                .build()
            chain.proceed(request)
        })
        .build()

    val api: ChatApi = Retrofit.Builder()
        .baseUrl(Env.baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ChatApi::class.java)
}
