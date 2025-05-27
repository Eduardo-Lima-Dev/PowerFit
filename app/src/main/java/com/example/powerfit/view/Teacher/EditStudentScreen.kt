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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.model.Student
import com.example.powerfit.model.StudentViewModel
import com.example.powerfit.model.UserSessionViewModel

@Composable
fun EditStudentScreen(navController: NavController, studentId: Int, userViewModel: UserSessionViewModel) {

    val user by userViewModel.user

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

    var editedName by remember { mutableStateOf(student.name) }
    var editedAge by remember { mutableStateOf(student.age.toString()) }
    var editedWeight by remember { mutableStateOf(student.weight.toString()) }
    var editedTrains by remember { mutableStateOf(student.trains) }
    var editedComorbidity by remember { mutableStateOf(student.hasComorbidity) }

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
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
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
                    text = "Editar Aluno",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campos editáveis
            OutlinedTextField(
                value = editedName,
                onValueChange = { editedName = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = editedAge,
                    onValueChange = { editedAge = it },
                    label = { Text("Idade") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = editedWeight,
                    onValueChange = { editedWeight = it },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Switches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pratica exercícios regularmente?")
                Switch(
                    checked = editedTrains,
                    onCheckedChange = { editedTrains = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Possui comorbidades?")
                Switch(
                    checked = editedComorbidity,
                    onCheckedChange = { editedComorbidity = it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão para Editar Treinos
            OutlinedButton(
                onClick = {
                    navController.navigate("editWorkouts/${student.id}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Treinos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Salvar
            Button(
                onClick = {
                    val updatedStudent = student.copy(
                        name = editedName,
                        age = editedAge.toIntOrNull() ?: 0,
                        weight = editedWeight.toFloatOrNull() ?: 0f,
                        trains = editedTrains,
                        hasComorbidity = editedComorbidity
                    )
                    viewModel.updateVinculatedStudent(updatedStudent)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }

        BottomMenu(navController = navController, modifier = Modifier.align(Alignment.BottomCenter), userViewModel)
    }
}