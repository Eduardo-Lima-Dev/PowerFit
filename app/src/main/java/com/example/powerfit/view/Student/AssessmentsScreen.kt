package com.example.powerfit.view.Student

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.powerfit.R
import com.example.powerfit.model.StudentAssessment
import com.example.powerfit.model.UserSessionViewModel
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.ui.theme.CustomNavigationButton

@Composable
fun AssessmentsScreen(navController: NavController, viewModel: UserSessionViewModel) {
    val user by viewModel.user
    val coroutineScope = rememberCoroutineScope()
    var student by remember { mutableStateOf<StudentAssessment?>(null) }

    // Buscar dados ao montar a tela
    LaunchedEffect(Unit) {
        student = viewModel.getLinkedStudent()
    }

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
                imageVector = Icons.Filled.ArrowBack,
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
            // Imagem de Perfil
            user?.let{
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

                // Nome do Usuário
                Text(
                    text = "Olá, ${it.name}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            student?.let {
                CustomNavigationButton(
                    text = "Idade: ${it.age} anos",
                    navRoute = "",
                    navController = navController,
                    clickable = false
                )

                CustomNavigationButton(
                    text = "Peso: ${it.weight} kg",
                    navRoute = "",
                    navController = navController,
                    clickable = false
                )

                CustomNavigationButton(
                    text = "Treina: ${if (it.trains) "Sim" else "Não"}",
                    navRoute = "",
                    navController = navController,
                    clickable = false
                )

                CustomNavigationButton(
                    text = "Comorbidade: ${if (it.hasComorbidity) "Sim" else "Não"}",
                    navRoute = "",
                    navController = navController,
                    clickable = false
                )
            }
        }

        BottomMenu(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter),
            viewModel = viewModel
        )
    }
}