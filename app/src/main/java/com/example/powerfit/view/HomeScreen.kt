package com.example.powerfit.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.powerfit.controller.HomeController
import com.example.powerfit.R
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.ui.theme.CustomNavigationButton

@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController = NavController(LocalContext.current)) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
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

            // Nome do Usuário
            Text(
                text = "Olá, ${user.name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Dias da Semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val daysOfWeek = listOf("D", "S", "T", "Q", "Q", "S", "S")

                daysOfWeek.forEachIndexed { index, day ->
                    Text(
                        text = day,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (index % 2 != 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        }
                    )
                }
            }

            // Bolinhas abaixo dos dias da semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val daysOfWeek = listOf("D", "S", "T", "Q", "Q", "S", "S")

                daysOfWeek.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (index % 2 != 0) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                }
                            )
                    )
                }
            }

            // Botões de Ação
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomNavigationButton(
                    text = "Treinos",
                    navRoute = "exercises",
                    navController = navController,
                    paddingTop = 50.dp,
                    icon = R.drawable.weight_icon,
                    iconSize = 50.dp
                )

                CustomNavigationButton(
                    text = "Avaliações",
                    navRoute = "assessments",
                    navController = navController,
                    paddingTop = 30.dp,
                    icon = R.drawable.assessment_icon,
                    iconSize = 50.dp
                )

                CustomNavigationButton(
                    text = "Progresso",
                    navRoute = "progress",
                    navController = navController,
                    paddingTop = 30.dp,
                    icon = R.drawable.progress_icon,
                    iconSize = 50.dp
                )
            }
        }

        // Menu Inferior de Navegação (com fundo escuro)
        BottomMenu(NavController(LocalContext.current))
    }
}