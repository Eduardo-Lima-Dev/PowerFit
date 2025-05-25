package com.example.powerfit.controller.chat

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

interface ChatApi {
    @Headers("Content-Type: application/json")
    @POST("api/v1/chat/completions")
    fun sendMessage(
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
