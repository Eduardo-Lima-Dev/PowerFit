package com.example.powerfit.controller.chat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

object RetrofitClient {
    private const val BASE_URL = "https://openrouter.ai/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer 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")
                .build()
            chain.proceed(request)
        })
        .build()

    val api: ChatApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ChatApi::class.java)
}
