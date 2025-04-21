package com.example.powerfit.model
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Student(
    val id: Int,
    val name: String,
    val age: Int,
    val trains: Boolean,
    val hasComorbidity: Boolean,
    val weight: Float
)

class StudentViewModel : ViewModel() {

    // Lista de estudantes cadastrados no sistema (não vinculados)
    var registeredStudents = mutableStateListOf(
        Student(1, "Ana Paula", 22, true, false, 58.5f),
        Student(2, "Bruno Souza", 27, false, true, 72.0f),
        Student(3, "Carlos Eduardo", 19, true, false, 68.3f),
        Student(4, "Daniela Lima", 25, false, false, 61.4f)
    )
        private set

    // Lista de estudantes já vinculados
    var vinculatedStudents = mutableStateListOf(
        Student(5, "Fernanda Oliveira", 29, true, true, 65.2f),
        Student(6, "Gustavo Martins", 21, false, false, 70.7f)
    )
        private set

    // Atualiza os dados de um aluno cadastrado
    fun updateRegisteredStudent(updated: Student) {
        val index = registeredStudents.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            registeredStudents[index] = updated
        }
    }

    // Atualiza os dados de um aluno já vinculado
    fun updateVinculatedStudent(updated: Student) {
        val index = vinculatedStudents.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            vinculatedStudents[index] = updated
        }
    }

    // Move um aluno da lista de cadastrados para a de vinculados
    fun vinculateStudent(student: Student) {
        val index = registeredStudents.indexOfFirst { it.id == student.id }
        if (index != -1) {
            val studentToMove = registeredStudents.removeAt(index)
            vinculatedStudents.add(studentToMove)
        }
    }
}