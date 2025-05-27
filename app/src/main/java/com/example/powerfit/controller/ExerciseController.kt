package com.example.powerfit.controller

import androidx.navigation.NavController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.ExerciseSet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciseController(private val navController: NavController) {
    private val db = FirebaseFirestore.getInstance()
    private val exercisesCollection = db.collection("exercises")

    // StateFlow para armazenar exercícios carregados
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises = _exercises.asStateFlow()

    // Carrega todos os exercícios do aluno específico
    suspend fun loadExercises(studentId: String) {
        try {
            val snapshot = exercisesCollection
                .whereEqualTo("studentId", studentId)
                .get()
                .await()

            val exerciseList = snapshot.documents.mapNotNull {
                it.toObject(Exercise::class.java)
            }

            _exercises.value = exerciseList
        } catch (e: Exception) {
            // Tratar erro - poderia registrar um log ou notificar o usuário
        }
    }

    // Obtém exercícios por categoria e ID do estudante
    fun getExercisesByCategory(category: String): List<Exercise> {
        return _exercises.value.filter { it.category == category }
    }

    // Obtém exercícios por categoria e studentId diretamente do Firebase
    fun getExercisesByCategoryFromFirebase(category: String, studentId: String, callback: (List<Exercise>) -> Unit) {
        exercisesCollection
            .whereEqualTo("category", category)
            .whereEqualTo("studentId", studentId)
            .get()
            .addOnSuccessListener { documents ->
                val exercises = documents.mapNotNull { it.toObject(Exercise::class.java) }
                callback(exercises)
            }
            .addOnFailureListener { _ ->
                callback(emptyList())
            }
    }

    // Obtém um exercício pelo ID
    fun getExerciseById(id: String): Exercise? {
        return _exercises.value.find { it.id == id }
    }

    // Adiciona um novo exercício
    suspend fun addExercise(exercise: Exercise): Boolean {
        return try {
            val documentRef = exercisesCollection.document()
            val exerciseWithId = exercise.copy(id = documentRef.id)
            documentRef.set(exerciseWithId).await()

            // Atualiza o estado local
            val currentExercises = _exercises.value.toMutableList()
            currentExercises.add(exerciseWithId)
            _exercises.value = currentExercises

            true
        } catch (e: Exception) {
            false
        }
    }

    // Atualiza um exercício existente
    suspend fun updateExercise(exercise: Exercise): Boolean {
        return try {
            exercisesCollection.document(exercise.id).set(exercise).await()

            // Atualiza o estado local
            val currentExercises = _exercises.value.toMutableList()
            val index = currentExercises.indexOfFirst { it.id == exercise.id }
            if (index >= 0) {
                currentExercises[index] = exercise
                _exercises.value = currentExercises
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    // Remove um exercício
    suspend fun deleteExercise(exerciseId: String): Boolean {
        return try {
            exercisesCollection.document(exerciseId).delete().await()

            // Atualiza o estado local
            val currentExercises = _exercises.value.toMutableList()
            currentExercises.removeAll { it.id == exerciseId }
            _exercises.value = currentExercises

            true
        } catch (e: Exception) {
            false
        }
    }
}