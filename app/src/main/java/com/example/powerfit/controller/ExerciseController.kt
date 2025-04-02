package com.example.powerfit.controller

import androidx.navigation.NavController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.ExerciseSet

class ExerciseController(private val navController: NavController) {

    // Mock data for exercises
    private val exercises = listOf(
        Exercise(
            id = "1",
            name = "Rosca Direta",
            videoUrl = "Q8TqfD8E7BU?si=3zw2jbz3kIJlYC8s",  // Remover https://youtu.be/
            sets = listOf(
                ExerciseSet(sets = 3, reps = 10)
            ),
            description = "Exercício para bíceps com barra reta"
        ),
        Exercise(
            id = "2",
            name = "Agachamento",
            videoUrl = "aclHkVaku9U",  // Remover https://www.youtube.com/watch?v=
            sets = listOf(
                ExerciseSet(sets = 4, reps = 12)
            ),
            description = "Exercício para pernas"
        ),
        Exercise(
            id = "3",
            name = "Supino Reto",
            videoUrl = "vthMCtgVtFw",  // Remover https://www.youtube.com/watch?v=
            sets = listOf(
                ExerciseSet(sets = 3, reps = 8)
            ),
            description = "Exercício para peitoral"
        )
    )

    fun getExerciseById(id: String): Exercise {
        return exercises.find { it.id == id } ?: exercises[0]
    }

    fun getAllExercises(): List<Exercise> {
        return exercises
    }
}
