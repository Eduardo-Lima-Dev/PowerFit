package com.example.powerfit.Controller

import androidx.navigation.NavController
import com.example.powerfit.Model.User
import com.example.powerfit.R

class HomeController(navController: NavController) {
    private val user = User(
        name = "Narak",
        email = "narak@example.com",
        password = "password123",
        confirmPassword = "password123",
        profileImage = R.drawable.profile_image
    )

    fun getUser() = user
}
