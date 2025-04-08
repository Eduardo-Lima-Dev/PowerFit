package com.example.powerfit.controller

class ChartController {
    // Método que retorna dados mockados para o gráfico
    fun getChartData(): List<Pair<String, Float>> {
        return listOf(
            "Seg" to 2f,
            "Ter" to 4f,
            "Qua" to 1f,
            "Qui" to 3f,
            "Sex" to 5f,
            "Sáb" to 2f,
            "Dom" to 4f
        )
    }
}