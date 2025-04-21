package com.example.powerfit.controller

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.powerfit.model.Exercise

class ExerciseViewModel : ViewModel() {
    // Mapa para armazenar os IDs dos exercícios concluídos
    private val _completedExercises = mutableStateMapOf<String, Boolean>()
    val completedExercises: Map<String, Boolean> = _completedExercises

    val exercises = mutableListOf<Exercise>()

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
}