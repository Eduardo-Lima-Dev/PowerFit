package com.example.powerfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.powerfit.Controller.LoginController
import com.example.powerfit.View.LoginScreen

class MainActivity : ComponentActivity() {
    private lateinit var controller: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = LoginController()
        setContent {
            LoginScreen(controller = controller)
        }
    }
}