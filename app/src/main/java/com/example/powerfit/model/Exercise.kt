package com.example.powerfit.model

data class Exercise(
    val id: String,
    val name: String,
    val videoUrl: String,
    val sets: List<ExerciseSet>,
    val description: String
)

data class ExerciseSet(
    val sets: Int,
    val reps: Int,
    val weight: Float = 0f
)
