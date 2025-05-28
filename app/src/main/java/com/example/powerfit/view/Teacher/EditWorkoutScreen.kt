package com.example.powerfit.view.Teacher

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.controller.ExerciseController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.Student
import com.example.powerfit.model.StudentViewModel
import com.example.powerfit.model.UserSessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutsScreen(navController: NavController, studentId: String, userViewModel: UserSessionViewModel) {
    Log.d("EditWorkoutsScreen", "Iniciando tela com studentId: $studentId")
    val user by userViewModel.user

    val studentViewModel: StudentViewModel = viewModel()
    val student by remember {
        derivedStateOf {
            studentViewModel.vinculatedStudents.value.find { it.id == studentId } ?: Student(
                id = studentId,
                name = "Aluno não encontrado",
                age = 0,
                trains = false,
                hasComorbidity = false,
                weight = 0f
            )
        }
    }

    val exerciseController = remember { ExerciseController(navController) }
    val categories = listOf("Superior", "Costas", "Inferior")
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var expanded by remember { mutableStateOf(false) }

    // Variável para forçar atualização da lista após exclusões
    var refreshTrigger by remember { mutableStateOf(0) }

    val exercisesInCategory by produceState(initialValue = emptyList<Exercise>(), selectedCategory, studentId, refreshTrigger) {
        try {
            exerciseController.getExercisesByCategoryFromFirebase(selectedCategory, studentId) { exercises ->
                value = exercises
            }
        } catch (e: Exception) {
            Log.e("EditWorkoutsScreen", "Erro ao buscar exercícios: ${e.message}", e)
            value = emptyList()
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
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, "Voltar")
                }

                Text(
                    text = "Editar Treinos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Informações do aluno
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
                        text = "Idade: ${student.age} anos | Peso: ${student.weight} kg",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Dropdown de categorias e botão de adicionar (removido botão de excluir)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedCategory,
                        onValueChange = {},
                        label = { Text("Categoria de Exercícios") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expandir categorias"
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        // Navegar para a tela de adicionar treino
                        navController.navigate("addExercise/${studentId}/${selectedCategory}")
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Adicionar")
                        Text("Exercício")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de exercícios
            Text(
                text = "Exercícios de $selectedCategory",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Box para mostrar exercícios ou mensagem quando não há exercícios
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (exercisesInCategory.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Nenhum exercício cadastrado nesta categoria para este aluno",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Adicione um novo exercício usando o botão acima",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(exercisesInCategory) { exercise ->
                            var showDeleteExerciseDialog by remember { mutableStateOf(false) }

                            ExerciseItem(
                                exercise = exercise,
                                onEditClick = {
                                    // Navegar para a tela de edição de exercício específico
                                    navController.navigate("editExercise/${studentId}/${exercise.id}")
                                },
                                onDeleteClick = {
                                    // Mostrar diálogo de confirmação para deletar o exercício
                                    showDeleteExerciseDialog = true
                                }
                            )

                            // Diálogo de confirmação para exclusão de exercício individual
                            if (showDeleteExerciseDialog) {
                                AlertDialog(
                                    onDismissRequest = { showDeleteExerciseDialog = false },
                                    title = { Text("Confirmação") },
                                    text = { Text("Deseja realmente excluir o exercício '${exercise.name}'?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                // Excluir exercício e atualizar a lista
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    exerciseController.deleteExercise(exercise.id)
                                                    // Incrementar o trigger para forçar a recarga dos dados
                                                    refreshTrigger++
                                                    showDeleteExerciseDialog = false
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            )
                                        ) {
                                            Text("Excluir")
                                        }
                                    },
                                    dismissButton = {
                                        OutlinedButton(onClick = { showDeleteExerciseDialog = false }) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                            }

                            Divider()
                        }
                    }
                }
            }
        }

        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter), userViewModel)
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "${exercise.sets.firstOrNull()?.sets ?: 0} séries × ${exercise.sets.firstOrNull()?.reps ?: 0} repetições",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Botões agrupados lado a lado
        Row {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar exercício",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir exercício",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}