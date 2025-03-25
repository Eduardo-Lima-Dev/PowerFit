package com.example.powerfit.Model

data class User(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
) {
    fun isValid(): Boolean {
        // Validação básica de dados
        return email.isNotEmpty() &&
                password.length >= 6 &&
                password == confirmPassword
    }
}
