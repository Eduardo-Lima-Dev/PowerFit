package com.example.powerfit.model

import com.example.powerfit.R

enum class Role { USER, TEACHER }

data class User(
    val id : Int,
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

object MockAuth {
    private val mockUsers = listOf(
        User(
            id = 0,
            name = "Professora Sora",
            email = "prof@fit.com",
            password = "senhaProf",
            confirmPassword = "senhaProf",
            profileImage = R.drawable.profile_icon,
            role = Role.TEACHER
        ),
        User(
            id = 1,
            name = "Aluno Luno",
            email = "aluno@fit.com",
            password = "senhaAluno",
            confirmPassword = "senhaAluno",
            profileImage = R.drawable.profile_icon,
            role = Role.USER
        )
    )

    var currentUser: User? = null

    fun login(email: String, password: String): Boolean {
        val user = mockUsers.find { it.email == email && it.password == password }
        currentUser = user
        return user != null
    }

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean = currentUser != null

    fun isTeacher(): Boolean = currentUser?.role == Role.TEACHER
}