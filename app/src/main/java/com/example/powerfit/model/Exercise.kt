package com.example.powerfit.model

data class Exercise(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val videoUrl: String = "",
    val sets: List<ExerciseSet> = emptyList(),
    val description: String = "",
    val studentId: String = ""
)

data class ExerciseSet(
    val sets: Int = 0,
    val reps: Int = 0,
    val weight: Float = 0.0f
)
