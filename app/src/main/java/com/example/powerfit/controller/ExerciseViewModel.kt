package com.example.powerfit.controller

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.powerfit.model.Exercise
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

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

    fun markExerciseDone(exerciseId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

        Firebase.firestore.collection("user_progress")
            .document(userId ?: return)
            .set(mapOf(today to true), SetOptions.merge())

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

    fun loadExercisesFromFirestore(category: String, callback: (Boolean) -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("exercises")
            .whereEqualTo("studentId", userId)
            .get()
            .addOnSuccessListener { result ->
                exercises.clear()
                for (document in result) {
                    val exercise = document.toObject(Exercise::class.java).copy(id = document.id)
                    exercises.add(exercise)
                }
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun toggleExerciseTodayStatus(exerciseId: String, onResult: (Boolean) -> Unit = {}) {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val docRef = Firebase.firestore.collection("exercise_progress").document(exerciseId)

        docRef.get().addOnSuccessListener { doc ->
            val current = doc.getBoolean(today) == true
            val newStatus = !current

            docRef.update(today, newStatus)
                .addOnSuccessListener {
                    onResult(newStatus)
                }
                .addOnFailureListener {
                    // Caso o campo não exista ainda
                    docRef.set(mapOf(today to newStatus), SetOptions.merge()).addOnSuccessListener {
                        onResult(newStatus)
                    }
                }
        }
    }

    fun isExerciseCompletedToday(exerciseId: String, onResult: (Boolean) -> Unit) {
        val todayKey = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

        Firebase.firestore.collection("exercise_progress")
            .document(exerciseId)
            .get()
            .addOnSuccessListener { doc ->
                val completed = doc.getBoolean(todayKey) == true
                onResult(completed)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }


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