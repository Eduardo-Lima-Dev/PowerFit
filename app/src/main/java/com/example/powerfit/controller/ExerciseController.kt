package com.example.powerfit.controller

import androidx.navigation.NavController
import com.example.powerfit.model.Exercise
import com.example.powerfit.model.ExerciseSet

class ExerciseController(private val navController: NavController) {
    private val allExercises = listOf(
        "Superior" to listOf(
            Exercise(
                id = "1",
                name = "Rosca Direta",
                category = "Superior",
                videoUrl = "Q8TqfD8E7BU",
                sets = listOf(ExerciseSet(sets = 3, reps = 10)),
                description = "Rosca Direta é um exercício que trabalha os músculos do bíceps..."
            ),
            Exercise(
                id = "2",
                name = "Elevacao Lateral",
                videoUrl = "video_id_2",
                category = "Superior",
                sets = listOf(ExerciseSet(sets = 3, reps = 12)),
                description = "Elevação Lateral é um exercício que trabalha os músculos dos ombros..."
            ),
            Exercise(
                id = "3",
                name = "Rosca Martelo",
                category = "Superior",
                videoUrl = "video_id_3",
                sets = listOf(ExerciseSet(sets = 3, reps = 10)),
                description = "Rosca Martelo é um exercício de musculação que foca no bíceps e antebraço..."
            )
        ),
        "Costas" to listOf(
            Exercise(
                id = "4",
                name = "Remada Curva",
                category = "Costas",
                videoUrl = "video_id_4",
                sets = listOf(ExerciseSet(sets = 3, reps = 10)),
                description = "Remada Curva é um exercício que trabalha os músculos das costas..."
            ),
            Exercise(
                id = "5",
                name = "Flexao de Ombro",
                category = "Costas",
                videoUrl = "video_id_5",
                sets = listOf(ExerciseSet(sets = 3, reps = 15)),
                description = "Flexão de Ombro é um exercício que trabalha os músculos do ombro e parte superior das costas..."
            )
        ),
        "Peito" to listOf(
            Exercise(
                id = "6",
                name = "Supino Reto",
                category = "Peito",
                videoUrl = "video_id_6",
                sets = listOf(ExerciseSet(sets = 3, reps = 10)),
                description = "Supino Reto é um exercício de musculação que foca no peitoral..."
            ),
            Exercise(
                id = "7",
                name = "Supino Inclinado",
                category = "Peito",
                videoUrl = "video_id_7",
                sets = listOf(ExerciseSet(sets = 3, reps = 12)),
                description = "Supino Inclinado é um exercício de musculação que foca no peitoral superior..."
            )
        ),
        "Inferior" to listOf(
            Exercise(
                id = "8",
                name = "Agachamento",
                category = "Inferior",
                videoUrl = "video_id_8",
                sets = listOf(ExerciseSet(sets = 3, reps = 12)),
                description = "Agachamento é um exercício que trabalha principalmente os músculos das pernas..."
            ),
        ),
    )
    private val exerciseMap = allExercises.flatMap { it.second }.associateBy { it.id }

    fun getExerciseById(id: String): Exercise? {
        return exerciseMap[id]
    }

    fun getExercisesByCategory(category: String) = allExercises.flatMap { it.second }.filter { it.category == category }
}