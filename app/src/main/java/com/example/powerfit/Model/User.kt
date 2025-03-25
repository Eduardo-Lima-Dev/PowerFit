package com.example.powerfit.Model

data class User(val email: String, val password: String) {
    fun isValid(): Boolean {
        // Valide o e-mail e a senha conforme necessário
        return email.isNotEmpty() && password.length >= 6
    }
}