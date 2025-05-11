package com.example.powerfit.view.Student

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.controller.ExerciseController
import com.example.powerfit.controller.ExerciseViewModel
import com.example.powerfit.controller.HomeController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.MockAuth

@Composable
fun ExerciseListScreen(
    navController: NavController,
    category: String,
    viewModel: ExerciseViewModel
) {
    // Redirecionar para login caso não esteja logado
    if (!MockAuth.isLoggedIn()) {
        navController.navigate("login") {
            popUpTo(0) // Limpa toda a pilha de navegação
        }
    }

    val controller = remember { ExerciseController(navController) }
    val homeController = remember { HomeController(navController) }
    val user = homeController.getUser()

    val exercises = try {
        controller.getExercisesByCategory(category)
    } catch (e: Exception) {
        emptyList<Exercise>()
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
        // Botão de voltar
        IconButton(
            onClick = { navController.navigateUp() },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nome do App
            Text(
                text = "PowerFit",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            // Imagem de Perfil
            user?.let{
                val painter = rememberAsyncImagePainter(model = it.profileImage)

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

            // Título da categoria
            Text(
                text = "Exercícios - $category",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Lista de exercícios
            if (exercises.isEmpty()) {
                Text("Nenhum exercício encontrado para esta categoria")
            } else {
                LazyColumn {
                    items(exercises) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            navController = navController,
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    navController: NavController,
    viewModel: ExerciseViewModel
) {
    // Use o estado do ViewModel em vez de um estado local
    val isCompleted = viewModel.isExerciseCompleted(exercise.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Conteúdo do exercício
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(8.dp))

                exercise.sets.forEach { set ->
                    Text(
                        text = "${set.sets}x${set.reps} repetições",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            Row {
                IconButton(onClick = { viewModel.toggleExerciseCompletion(exercise.id) }) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Done,
                        contentDescription = "Marcar como concluído",
                        tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("exerciseView/${exercise.id}")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Ver exercício",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}