package com.example.powerfit.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.powerfit.R
import com.example.powerfit.model.Role
import com.example.powerfit.model.UserSessionViewModel
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.ui.theme.CustomNavigationButton

@Composable
fun SettingsScreen(navController: NavController, viewModel: UserSessionViewModel) {
    val user by viewModel.user

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        IconButton(
            onClick = {
                when (user?.role) {
                    Role.TEACHER -> { navController.navigate("teacherHome") }
                    Role.USER -> { navController.navigate("home") }
                    else -> { navController.navigate("login") }
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            user?.let {
                val painter = rememberAsyncImagePainter(it.profileImage)

                Image(
                    painter = painter,
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            user?.let {
                Text(
                    text = it.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            user?.let {
                Text(
                    text = it.email,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CustomNavigationButton(
                text = "Alterar foto de perfil",
                navRoute = "profilePhoto",
                navController = navController,
                clickable = true
            )

            CustomNavigationButton(
                text = "Chatbot",
                navRoute = "chatBot",
                navController = navController
            )

            CustomNavigationButton(
                text = "Alterar email",
                navRoute = "changeEmail",
                navController = navController
            )

            CustomNavigationButton(
                text = "Alterar senha",
                navRoute = "changePassword",
                navController = navController
            )

            CustomNavigationButton(
                text = "Sair",
                navRoute = "login",
                navController = navController,
                clearBackStack = true,
                onBeforeNav = {
                    viewModel.clearUser()
                }
            )
        }
        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter), viewModel)
    }
}