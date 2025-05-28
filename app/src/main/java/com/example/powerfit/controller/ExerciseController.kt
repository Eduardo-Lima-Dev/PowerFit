package com.example.powerfit.controller

import android.util.Log
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
    fun getExerciseById(exerciseId: String, onSuccess: (Exercise?) -> Unit) {
        db.collection("exercises").document(exerciseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val exercise = document.toObject(Exercise::class.java)
                    onSuccess(exercise)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener {
                onSuccess(null)
            }
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
    // No arquivo ExerciseController.kt
    fun deleteExercisesByCategory(studentId: String, category: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("exercises")
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { documents ->
                val batch = db.batch()
                for (document in documents) {
                    batch.delete(document.reference)
                }
                batch.commit().addOnSuccessListener {
                    Log.d("ExerciseController", "Exercícios da categoria $category excluídos com sucesso")
                }.addOnFailureListener { e ->
                    Log.e("ExerciseController", "Erro ao excluir exercícios: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("ExerciseController", "Erro ao buscar exercícios para exclusão: ${e.message}")
            }
    }

    // Adicione este método ao ExerciseController.kt
    fun deleteExercise(exerciseId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("exercises")
            .document(exerciseId)
            .delete()
            .addOnSuccessListener {
                Log.d("ExerciseController", "Exercício excluído com sucesso")
            }
            .addOnFailureListener { e ->
                Log.e("ExerciseController", "Erro ao excluir exercício: ${e.message}")
            }
    }
}