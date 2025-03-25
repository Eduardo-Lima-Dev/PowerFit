package com.example.powerfit

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
import com.example.powerfit.Controller.LoginController
import com.example.powerfit.Controller.RegistrationController
import com.example.powerfit.View.LoginScreen
import com.example.powerfit.View.RegistrationScreen
import com.example.powerfit.ui.theme.PowerFitTheme

class MainActivity : ComponentActivity() {
    private lateinit var controller: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    NavHost(navController = navController, startDestination = "login") {
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
    }
}