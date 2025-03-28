package com.example.powerfit

import com.jakewharton.threetenabp.AndroidThreeTen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.powerfit.controller.LoginController
import com.example.powerfit.controller.RegistrationController
import com.example.powerfit.ui.theme.PowerFitTheme
import com.example.powerfit.view.ExerciseSelectionScreen
import com.example.powerfit.view.HomeScreen
import com.example.powerfit.view.LoginScreen
import com.example.powerfit.view.RecoverPasswordScreen
import com.example.powerfit.view.RecoverSentScreen
import com.example.powerfit.view.RegistrationScreen

class MainActivity : ComponentActivity() {
    private lateinit var controller: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        controller = LoginController()
        setContent {
            PowerFitTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SetupNavHost()
                }
            }
        }
    }
}

@Composable
fun SetupNavHost() {
    val navController = rememberNavController() // Criando o NavController aqui

    NavHost(navController = navController, startDestination = "home") {
        composable("login") {
            // Passando o navController para a tela de LoginScreen
            LoginScreen(navController = navController)
        }
        composable("register") {
            // Passando o navController e controller de RegistrationController para RegistrationScreen
            RegistrationScreen(
                navController = navController,
                controller = RegistrationController()
            )
        }
        composable("recover") {
            // Passando o navController para RecoverPasswordScreen
            RecoverPasswordScreen(
                navController = navController,
            )
        }
        composable("recoverSent") {
            // Passando o navController para RecoverSentScreen
            RecoverSentScreen(
                navController = navController,
            )
        }
        composable("home") {
            // Passando o navController para HomeScreen
            HomeScreen(navController = navController)
        }
        composable("exercises") {
            // Passando o navController para HomeScreen
            ExerciseSelectionScreen(navController = navController)
        }
    }
}