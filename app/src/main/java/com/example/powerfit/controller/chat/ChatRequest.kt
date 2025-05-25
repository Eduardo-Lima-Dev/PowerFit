package com.example.powerfit.controller.chat

import com.example.powerfit.controller.Env

data class ChatRequest(
    val model: String = Env.modelName,
    val messages: List<Message>
)

data class Message(
    val role: String, // "user" ou "assistant"
    val content: String
)

data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)
