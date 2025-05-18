package com.example.powerfit.controller

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.powerfit.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseViewModel : ViewModel() {
    // Mapa para armazenar os IDs dos exercícios concluídos
    private val _completedExercises = mutableStateMapOf<String, Boolean>()
    val completedExercises: Map<String, Boolean> = _completedExercises

    val exercises = mutableListOf<Exercise>()

    // Mapa para armazenar os exercícios por categoria
    private val _exercisesByCategory = mutableStateOf<List<Exercise>>(listOf())
//    val exercisesByCategory: State<List<Exercise>> = _exercisesByCategory

    /** <<<<<  ADICIONE ISTO  */
    fun exercisesByCategory(category: String): List<Exercise> =
        exercises.filter { it.category == category }

    fun getExerciseById(exerciseId: String): Exercise? {
        return exercises.find { it.id == exerciseId }
    }

    // Função para alternar o estado de conclusão
    fun toggleExerciseCompletion(exerciseId: String) {
        val currentValue = _completedExercises[exerciseId] ?: false
        _completedExercises[exerciseId] = !currentValue
    }

    // Verifica se um exercício está concluído
    fun isExerciseCompleted(exerciseId: String): Boolean {
        return _completedExercises[exerciseId] ?: false
    }

    // No ExerciseViewModel.kt
    fun saveExercise(exercise: Exercise, category: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val exerciseWithoutId = exercise.copy()

        db.collection("users")
            .document(userId)
            .collection("exercises")
            .add(exerciseWithoutId)
            .addOnSuccessListener { documentReference ->
                // Atualize o cache local com o novo exercício
                val newExercise = exercise.copy(id = documentReference.id)
                exercises.add(newExercise)
                // Atualiza a lista por categoria
                val updatedList = exercises.filter { it.category == category }
                _exercisesByCategory.value = updatedList

                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("ExerciseViewModel", "Erro ao salvar exercício: ${e.message}")
                callback(false)
            }
    }
}