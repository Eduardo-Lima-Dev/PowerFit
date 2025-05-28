package com.example.powerfit.view.Student

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.controller.ExerciseController
import com.example.powerfit.controller.ExerciseViewModel
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.UserSessionViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun ExerciseListScreen(
    navController: NavController,
    category: String,
    viewModel: ExerciseViewModel,
    userViewModel: UserSessionViewModel
) {
    val user by userViewModel.user

    val exercises = remember { mutableStateOf<List<Exercise>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    // Delay de 1 segundo
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        isLoading.value = false
    }

    // Carregar exercícios do Firestore
    LaunchedEffect(category) {
        viewModel.loadExercisesFromFirestore(category) { success ->
            if (success) {
                exercises.value = viewModel.exercisesByCategory(category)
            }
        }
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
        if (isLoading.value) {
            // Indicador de carregamento
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
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
                Text(
                    text = "PowerFit",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

                Text(
                    text = "Exercícios - $category",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (exercises.value.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Nenhum exercício encontrado para esta categoria",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                } else {
                    LazyColumn {
                        items(exercises.value) { exercise ->
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

            BottomMenu(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter),
                userViewModel
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    navController: NavController,
    viewModel: ExerciseViewModel
) {
    val isCompleted = viewModel.isExerciseCompleted(exercise.id)

    val completedToday = remember { mutableStateOf(false) }

    // Carrega progresso do dia ao montar
    LaunchedEffect(exercise.id) {
        viewModel.isExerciseCompletedToday(exercise.id) { completed ->
            completedToday.value = completed
        }
    }

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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = if (completedToday.value) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(8.dp))

                exercise.sets.forEach { set ->
                    Text(
                        text = "${set.sets}x${set.reps} repetições",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (completedToday.value) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            Row {
                IconButton(onClick = {
                    viewModel.toggleExerciseTodayStatus(exercise.id) { newStatus ->
                        completedToday.value = newStatus
                    }
                }) {
                    Icon(
                        imageVector = if (completedToday.value) Icons.Default.CheckCircle else Icons.Default.Done,
                        contentDescription = "Marcar como concluído",
                        tint = if (completedToday.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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