package com.example.powerfit.view.Teacher

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.powerfit.R
import com.example.powerfit.model.Student
import com.example.powerfit.model.StudentViewModel
import com.example.powerfit.model.UserSessionViewModel
import kotlin.text.compareTo
import kotlin.text.get
import kotlin.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentBindingScreen(navController: NavHostController, viewModel: UserSessionViewModel) {
    val user by viewModel.user

    val studentViewModel: StudentViewModel = viewModel()
    val studentsList = studentViewModel.usersWithRoleUser.value
    val isLoading = studentViewModel.isLoading.value

    var selectedStudentIndex by remember { mutableStateOf(0) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    // Estados para o formulário
    var trains by remember { mutableStateOf(false) }
    var comorbidity by remember { mutableStateOf(false) }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // Atualiza o estudante selecionado quando a lista muda
    LaunchedEffect(studentsList) {
        selectedStudent = if (studentsList.isNotEmpty() && selectedStudentIndex < studentsList.size) {
            studentsList[selectedStudentIndex]
        } else {
            null
        }
    }

    // Atualiza os valores quando o aluno selecionado muda
    LaunchedEffect(selectedStudent) {
        selectedStudent?.let {
            trains = it.trains
            comorbidity = it.hasComorbidity
            weight = it.weight.toString()
            age = it.age.toString()
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
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }

        // Mostrar indicador de carregamento
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "PowerFit Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 6.dp)
                )

                Text(
                    text = "Vincular Estudante",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 26.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Dropdown de nomes
                        var expanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedStudent?.name ?: "",
                                onValueChange = {},
                                label = { Text("Nome") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        Modifier.clickable { expanded = !expanded }
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                studentsList.forEachIndexed { index, student ->
                                    DropdownMenuItem(
                                        text = { Text(student.name) },
                                        onClick = {
                                            selectedStudentIndex = index
                                            selectedStudent = student
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = age,
                            onValueChange = { newValue ->
                                // Aceitar apenas números
                                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                    age = newValue
                                }
                            },
                            label = { Text("Idade") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Já treina?", modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = trains,
                                    onClick = { trains = true }
                                )
                                Text("Sim", modifier = Modifier.padding(end = 8.dp))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = !trains,
                                    onClick = { trains = false }
                                )
                                Text("Não")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Comorbidade?", modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = comorbidity,
                                    onClick = { comorbidity = true }
                                )
                                Text("Sim", modifier = Modifier.padding(end = 8.dp))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = !comorbidity,
                                    onClick = { comorbidity = false }
                                )
                                Text("Não")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                selectedStudent?.let {
                                    val updatedStudent = it.copy(
                                        trains = trains,
                                        hasComorbidity = comorbidity,
                                        weight = weight.toFloatOrNull() ?: it.weight,
                                        age = age.toIntOrNull() ?: it.age
                                    )
                                    studentViewModel.updateRegisteredStudent(updatedStudent)
                                    studentViewModel.vinculateStudent(updatedStudent)
                                    navController.navigate("teacherHome")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = selectedStudent != null
                        ) {
                            Text(
                                "VINCULAR",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}