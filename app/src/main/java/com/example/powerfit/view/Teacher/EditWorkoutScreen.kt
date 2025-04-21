package com.example.powerfit.view.Teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powerfit.components.BottomMenu
import com.example.powerfit.controller.ExerciseController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.Student
import com.example.powerfit.model.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutsScreen(navController: NavController, studentId: Int) {
    val viewModel: StudentViewModel = viewModel()
    val student by remember { derivedStateOf {
        viewModel.vinculatedStudents.find { it.id == studentId } ?: Student(
            id = 0,
            name = "",
            age = 0,
            trains = false,
            hasComorbidity = false,
            weight = 0f
        )
    } }

    val exerciseController = remember { ExerciseController(navController) }
    val categories = listOf("Superior", "Costas", "Peito", "Inferior")
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var expanded by remember { mutableStateOf(false) }

    val exercisesInCategory = remember(selectedCategory) {
        exerciseController.getExercisesByCategory(selectedCategory)
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

            // Dropdown de categorias e botão de adicionar
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(exercisesInCategory) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        onEditClick = {
                            // Navegar para a tela de edição de exercício específico
                            navController.navigate("editExercise/${studentId}/${exercise.id}")
                        }
                    )
                    Divider()
                }
            }
        }

        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, onEditClick: () -> Unit) {
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

        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar exercício",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}