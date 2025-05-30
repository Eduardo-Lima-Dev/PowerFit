package com.example.powerfit.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.powerfit.model.UserSessionViewModel

@Composable
fun BottomMenu(navController: NavController, modifier: Modifier = Modifier, viewModel: UserSessionViewModel) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            when (viewModel.isTeacher()) {
                true -> { navController.navigate("teacherHome") }
                false -> { navController.navigate("home") }
                else -> { navController.navigate("login") }
            }
        }) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
        IconButton(onClick = { navController.navigate("settings") }) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}