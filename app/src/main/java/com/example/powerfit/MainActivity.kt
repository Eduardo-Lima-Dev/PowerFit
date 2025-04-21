package com.example.powerfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.powerfit.controller.ExerciseViewModel
import com.example.powerfit.controller.RegistrationController
import com.example.powerfit.ui.theme.PowerFitTheme
import com.example.powerfit.view.Student.AssessmentsScreen
import com.example.powerfit.view.ChangeEmailScreen
import com.example.powerfit.view.ChangePasswordScreen
import com.example.powerfit.view.Student.ChartScreen
import com.example.powerfit.view.ChatScreen
import com.example.powerfit.view.Student.ExerciseListScreen
import com.example.powerfit.view.Student.ExerciseSelectionScreen
import com.example.powerfit.view.Student.HomeScreen
import com.example.powerfit.view.LoginScreen
import com.example.powerfit.view.RecoverPasswordScreen
import com.example.powerfit.view.RecoverSentScreen
import com.example.powerfit.view.RegistrationScreen
import com.example.powerfit.view.Student.ExerciseViewScreen
import com.example.powerfit.view.SettingsScreen
import com.example.powerfit.view.Teacher.StudentBindingScreen
import com.example.powerfit.view.Teacher.TeacherHomeScreen
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContent {
            val sharedViewModel: ExerciseViewModel = viewModel(viewModelStoreOwner = this)
            PowerFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavHost(sharedViewModel)
                }
            }
        }
    }
}

@Composable
fun SetupNavHost(sharedViewModel: ExerciseViewModel) {
    val navController = rememberNavController() // Criando o NavController aqui

    NavHost(navController = navController, startDestination = "teacherHome") {
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
        composable("exerciseView") {
            // Passando o navController para ExerciseViewScreen
            ExerciseViewScreen(navController = navController)
        }
        // Rota para lista de exercícios por ID
        composable(
            route = "exerciseView/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseViewScreen(navController = navController, exerciseId = exerciseId)
        }
        // Rota para lista de exercícios por categoria
        composable(
            route = "exerciseList/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            // Passando o sharedViewModel para o ExerciseListScreen
            ExerciseListScreen(navController = navController, category = category, viewModel = sharedViewModel)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("chart") {
            ChartScreen(navController = navController)
        }
        composable("assessments"){
            AssessmentsScreen(navController = navController)
        }
        composable("changeEmail"){
            ChangeEmailScreen(navController = navController)
        }
        composable("changePassword"){
            ChangePasswordScreen(navController = navController)
        }
        composable ("chatBot"){
            ChatScreen(navController = navController)
        }
        composable("teacherHome"){
            TeacherHomeScreen(navController)
        }
        composable("studentBinding"){
            StudentBindingScreen(navController)
        }
    }
}