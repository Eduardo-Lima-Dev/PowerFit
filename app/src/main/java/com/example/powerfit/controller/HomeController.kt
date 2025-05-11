package com.example.powerfit.controller

import androidx.navigation.NavController
import com.example.powerfit.model.User
import com.example.powerfit.R
import com.example.powerfit.model.Role

class HomeController(navController: NavController) {
    private val user = User(
        name = "Narak",
        email = "narak@example.com",
        profileImage = "R.drawable.profile_icon",
        role = Role.USER
    )

    private val teacher = User(
        name = "Braga",
        email = "prof@example.com",
        profileImage = "R.drawable.profile_icon",
        role = Role.TEACHER
    )

    fun getTeacher() = teacher

    fun getUser() = user
}
