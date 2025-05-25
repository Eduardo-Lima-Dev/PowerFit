package com.example.powerfit.controller.chat

data class ChatRequest(
    val model: String = "meta-llama/llama-3.3-8b-instruct:free",
    val messages: List<Message>
)

data class Message(
    val role: String, // "user" ou "assistant"
    val content: String
)

data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)
