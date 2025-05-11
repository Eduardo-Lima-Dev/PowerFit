package com.example.powerfit.model

import com.example.powerfit.R

enum class Role { USER, TEACHER }

data class User(
    val name: String = "",
    val email: String = "",
    val profileImage: String = "profile_icon",
    val role: Role? = null
)

object MockAuth {
    private val mockUsers = listOf(
        User(
            name = "Professora Sora",
            email = "prof@fit.com",
            profileImage = "profile_icon",
            role = Role.TEACHER
        ),
        User(
            name = "Aluno Luno",
            email = "aluno@fit.com",
            profileImage = "profile_icon",
            role = Role.USER
        )
    )

    var currentUser: User? = null

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean = currentUser != null
}