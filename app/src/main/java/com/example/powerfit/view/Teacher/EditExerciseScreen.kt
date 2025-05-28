package com.example.powerfit.view.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.controller.ExerciseController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.ExerciseSet
import com.example.powerfit.model.Student
import com.example.powerfit.model.StudentViewModel
import com.example.powerfit.model.UserSessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExerciseScreen(
    navController: NavController,
    studentId: String,
    category: String,
    exerciseId: String? = null,
    userViewModel: UserSessionViewModel
) {
    val user by userViewModel.user

    val viewModel: StudentViewModel = viewModel()
    val student by remember {
        derivedStateOf {
            viewModel.vinculatedStudents.value.find { it.id == studentId.toString() } ?: Student(
                id = "0",
                name = "",
                age = 0,
                trains = false,
                hasComorbidity = false,
                weight = 0f
            )
        }
    }

    val exerciseController = remember { ExerciseController(navController) }
    val isEditMode = exerciseId != null

    // Estados para controlar o formulário
    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var exerciseVideoUrl by remember { mutableStateOf("") }
    var exerciseSets by remember { mutableStateOf("3") }
    var exerciseReps by remember { mutableStateOf("12") }
    var exerciseWeight by remember { mutableStateOf("0") }
    var isLoading by remember { mutableStateOf(isEditMode) }

    // Carregar dados quando em modo de edição
    LaunchedEffect(exerciseId) {
        if (isEditMode && exerciseId != null) {
            exerciseController.getExerciseById(exerciseId) { exercise ->
                if (exercise != null) {
                    exerciseName = exercise.name ?: ""
                    exerciseDescription = exercise.description ?: ""
                    exerciseVideoUrl = exercise.videoUrl ?: ""
                    exerciseSets = exercise.sets?.firstOrNull()?.sets?.toString() ?: "3"
                    exerciseReps = exercise.sets?.firstOrNull()?.reps?.toString() ?: "12"
                    exerciseWeight = exercise.sets?.firstOrNull()?.weight?.toString() ?: "0"
                }
                isLoading = false
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
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 500.dp)
                    .padding(horizontal = 32.dp, vertical = 48.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }

                    Text(
                        text = if (isEditMode) "Editar Exercício" else "Adicionar Exercício",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.size(48.dp))
                }

                Spacer(modifier = Modifier.height(48.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Aluno: ${student.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Categoria: $category",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text("Nome do Exercício") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = exerciseDescription,
                    onValueChange = { exerciseDescription = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = exerciseVideoUrl,
                    onValueChange = { exerciseVideoUrl = it },
                    label = { Text("URL do Vídeo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Configuração do Treino",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = exerciseSets,
                        onValueChange = { exerciseSets = it },
                        label = { Text("Séries") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = exerciseReps,
                        onValueChange = { exerciseReps = it },
                        label = { Text("Repetições") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = exerciseWeight,
                        onValueChange = { exerciseWeight = it },
                        label = { Text("Peso (kg)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val newExercise = Exercise(
                            id = exerciseId ?: "novo_id",
                            name = exerciseName,
                            category = category,
                            videoUrl = exerciseVideoUrl,
                            sets = listOf(
                                ExerciseSet(
                                    sets = exerciseSets.toIntOrNull() ?: 3,
                                    reps = exerciseReps.toIntOrNull() ?: 12,
                                    weight = exerciseWeight.toFloatOrNull() ?: 0f
                                )
                            ),
                            description = exerciseDescription,
                            studentId = studentId
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            if (isEditMode) {
                                exerciseController.updateExercise(newExercise)
                            } else {
                                exerciseController.addExercise(newExercise)
                            }
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar Exercício")
                }
            }
        }

        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter), userViewModel)
    }
}