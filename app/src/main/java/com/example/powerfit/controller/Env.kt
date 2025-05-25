package com.example.powerfit.controller

import io.github.cdimascio.dotenv.dotenv

object Env {
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    val apiKey: String = dotenv["SECRET_API_KEY"]
    val baseUrl: String = dotenv["BASE_URL"]
    val modelName: String = dotenv["MODEL_NAME"]
}