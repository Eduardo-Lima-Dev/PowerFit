package com.example.powerfit.view.Student

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.view.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    androidx.compose.material3.Scaffold(
        bottomBar = {
            val currentRoute = currentRoute(navController)
            if (shouldShowBottomBar(currentRoute)) {
                BottomMenu(navController = navController)
            }
        }
    ) { innerPadding ->
        // Garanta que o NavigationHost está usando o padding corretamente
        NavigationHost(navController, innerPadding)
    }
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    padding: androidx.compose.foundation.layout.PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(padding)
    ) {
        composable("home") { HomeScreen(navController) }
        composable("exercises") { ExerciseViewScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

private fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf("home", "exercises", "settings")
}