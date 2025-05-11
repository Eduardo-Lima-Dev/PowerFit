package com.example.powerfit.controller

import com.example.powerfit.R
import com.example.powerfit.model.Role
import com.example.powerfit.model.User

class LoginController {
    fun authenticate(email: String, password: String): User? {
        return when {
            email == "narak@example.com" && password == "senha123" ->
                User(
                    name = "Narak",
                    email = email,
                    profileImage = "R.drawable.profile_icon",
                    role = Role.USER
                )
            email == "prof@example.com" && password == "senhaProf" ->
                User(
                    name = "Braga",
                    email = email,
                    profileImage = "R.drawable.profile_icon",
                    role = Role.TEACHER
                )
            else -> null
        }
    }
}