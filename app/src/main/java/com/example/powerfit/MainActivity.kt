package com.example.powerfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.powerfit.model.UserSessionViewModel
import com.example.powerfit.ui.theme.PowerFitTheme
import com.example.powerfit.view.ChangeEmailScreen
import com.example.powerfit.view.ChangePasswordScreen
import com.example.powerfit.view.ChatScreen
import com.example.powerfit.view.LoginScreen
import com.example.powerfit.view.ProfilePhotoScreen
import com.example.powerfit.view.RecoverPasswordScreen
import com.example.powerfit.view.RecoverSentScreen
import com.example.powerfit.view.RegistrationScreen
import com.example.powerfit.view.SettingsScreen
import com.example.powerfit.view.Student.AssessmentsScreen
import com.example.powerfit.view.Student.ChartScreen
import com.example.powerfit.view.Student.ExerciseListScreen
import com.example.powerfit.view.Student.ExerciseSelectionScreen
import com.example.powerfit.view.Student.ExerciseViewScreen
import com.example.powerfit.view.Student.HomeScreen
import com.example.powerfit.view.Teacher.EditExerciseScreen
import com.example.powerfit.view.Teacher.EditStudentScreen
import com.example.powerfit.view.Teacher.EditWorkoutsScreen
import com.example.powerfit.view.Teacher.StudentBindingScreen
import com.example.powerfit.view.Teacher.TeacherHomeScreen
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    private val userSessionViewModel: UserSessionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        AndroidThreeTen.init(applicationContext)
        setContent {
            val sharedViewModel: ExerciseViewModel = viewModel()
            PowerFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavHost(sharedViewModel, userSessionViewModel)
                }
            }
        }
    }
}

@Composable
fun SetupNavHost(sharedViewModel: ExerciseViewModel, userSessionViewModel: UserSessionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, userSessionViewModel)
        }
        composable("register") {
            RegistrationScreen(
                navController = navController,
                controller = RegistrationController(),
                userSessionViewModel
            )
        }
        composable("recover") {
            RecoverPasswordScreen(
                navController = navController,
                userSessionViewModel
            )
        }
        composable("recoverSent") {
            RecoverSentScreen(
                navController = navController,
            )
        }
        composable("home") {
            HomeScreen(navController = navController, userSessionViewModel)
        }
        composable("exercises") {
            ExerciseSelectionScreen(navController = navController, viewModel = sharedViewModel, userSessionViewModel)
        }
        composable("exerciseView") {
            val exerciseId = ""
            ExerciseViewScreen(navController = navController, exerciseId = exerciseId, viewModel = sharedViewModel, userSessionViewModel)
        }
        composable(
            route = "exerciseView/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseViewScreen(navController = navController, exerciseId = exerciseId, viewModel = sharedViewModel, userSessionViewModel)
        }
        composable(
            route = "exerciseList/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ExerciseListScreen(navController = navController, category = category, viewModel = sharedViewModel, userSessionViewModel)
        }
        composable("editStudent/{studentId}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            EditStudentScreen(
                navController = navController,
                studentId = studentId,
                userSessionViewModel
            )
        }
        composable("editWorkouts/{studentId}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            EditWorkoutsScreen(
                navController = navController,
                studentId = studentId,
                userSessionViewModel
            )
        }
        composable(
            route = "editExercise/{studentId}/{exerciseId}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("exerciseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            EditExerciseScreen(
                navController = navController,
                studentId = studentId,
                category = sharedViewModel.getExerciseById(exerciseId)?.category ?: "",
                exerciseId = exerciseId,
                userSessionViewModel
            )
        }
        composable(
            route = "addExercise/{studentId}/{category}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            EditExerciseScreen(
                navController = navController,
                studentId = studentId,
                category = category,
                exerciseId = exerciseId,
                userSessionViewModel
            )
        }
        composable("settings") {
            SettingsScreen(navController = navController, userSessionViewModel)
        }
        composable("chart") {
            ChartScreen(navController = navController, userSessionViewModel)
        }
        composable("assessments") {
            AssessmentsScreen(navController = navController, userSessionViewModel)
        }
        composable("changeEmail") {
            ChangeEmailScreen(navController = navController, userSessionViewModel)
        }
        composable("changePassword") {
            ChangePasswordScreen(navController = navController, userSessionViewModel)
        }
        composable("chatBot") {
            ChatScreen(navController = navController)
        }
        composable("teacherHome") {
            TeacherHomeScreen(navController, userSessionViewModel)
        }
        composable("studentBinding") {
            StudentBindingScreen(navController, userSessionViewModel)
        }
        composable("profilePhoto") {
            ProfilePhotoScreen(navController = navController, userSessionViewModel)
        }
    }
}