package com.example.powerfit.controller

import androidx.navigation.NavController
import com.example.powerfit.model.User
import com.example.powerfit.R
import com.example.powerfit.model.Role

class HomeController(navController: NavController) {
    private val user = User(
        name = "Narak",
        email = "narak@example.com",
        password = "password123",
        confirmPassword = "password123",
        profileImage = R.drawable.profile_icon,
        role = Role.USER
    )

    fun getUser() = user
}
