package com.example.powerfit.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.powerfit.R
import com.example.powerfit.controller.HomeController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.ui.theme.CustomNavigationButton

@Preview(showBackground = true)
@Composable
fun ExerciseSelectionScreen(navController: NavController = NavController(LocalContext.current)) {
    val controller = remember { HomeController(navController) }
    val user = controller.getUser()

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
            onClick = { navController.navigate("home") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Nome do App
            Text(
                text = "PowerFit",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            // Imagem de Perfil
            Image(
                painter = painterResource(id = user.profileImage),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(8.dp)
            )

            // Botões de Ação
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomNavigationButton(
                    text = "Superior",
                    navRoute = "exercises/superior",
                    navController = navController,
                    paddingTop = 50.dp,
                    icon = R.drawable.superior_icon,
                    iconSize = 50.dp
                )

                CustomNavigationButton(
                    text = "Costas",
                    navRoute = "exercises/back",
                    navController = navController,
                    paddingTop = 30.dp,
                    icon = R.drawable.back_icon,
                    iconSize = 50.dp
                )

                CustomNavigationButton(
                    text = "Inferior",
                    navRoute = "exercises/back",
                    navController = navController,
                    paddingTop = 30.dp,
                    icon = R.drawable.inferior_icon,
                    iconSize = 50.dp
                )
            }
        }

        // Menu Inferior de Navegação (com fundo escuro)
        BottomMenu(NavController(LocalContext.current))
    }
}