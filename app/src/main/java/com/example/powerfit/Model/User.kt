package com.example.powerfit.Model

data class User(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val profileImage: Int
) {
    fun isValid(): Boolean {
        return email.isNotEmpty() && password.length >= 6 && password == confirmPassword
    }
}
