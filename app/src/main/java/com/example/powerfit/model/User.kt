package com.example.powerfit.model

enum class Role { USER, TEACHER }

data class User(
    val name: String = "",
    val email: String = "",
    val profileImage: String? = null,
    val role: Role? = null
)