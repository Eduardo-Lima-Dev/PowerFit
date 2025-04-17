package com.example.powerfit.model

enum class Role { USER, TEACHER }

data class User(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val profileImage: Int,
    val role: Role?
) {
    fun isValid(): Boolean {
        return email.isNotEmpty()
                && password.length >= 6
                && password == confirmPassword
    }
}