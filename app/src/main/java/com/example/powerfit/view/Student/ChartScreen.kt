@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.powerfit.view.Student

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.powerfit.model.UserSessionViewModel

@Composable
fun ChartScreen(navController: NavController, viewModel: UserSessionViewModel) {
    val user by viewModel.user
    val uid by viewModel.uid
    val scope = rememberCoroutineScope()

    // Estado para os dados do gráfico: lista de (diaAbreviado, valor)
    val chartDataState = produceState<List<Pair<String, Float>>>(
        initialValue = emptyList(),
        key1 = uid
    ) {
        val userId = uid
        if (userId != null) {
            try {
                val presenceMap = viewModel.fetchWeeklyPresenceCounts(userId)
                // Ordena por DayOfWeek.value (1=Monday..7=Sunday), converte pra abreviação e Float
                val listForChart = presenceMap.map { (dia, count) ->
                    val dias = dia.toString()
                    dias.take(3) to count.toFloat()
                }
                value = listForChart as List<Pair<String, Float>>
            } catch (e: Exception) {
                value = emptyList()
            }
        } else {
            value = emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dias Ativos por Dia da Semana") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Visão Geral",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (chartDataState.value.isEmpty()) {
                    // Pode exibir loading ou mensagem
                    Text("Carregando dados...", modifier = Modifier.align(Alignment.Center))
                } else {
                    SimpleLineChart(chartData = chartDataState.value)
                }
            }
        }
    }
}

@Composable
fun SimpleLineChart(chartData: List<Pair<String, Float>>) {
    val isDarkTheme = isSystemInDarkTheme()
    if (chartData.isEmpty()) return

    val maxValue = chartData.maxOf { it.second }.coerceAtLeast(1f) // evita div por zero

    Canvas(modifier = Modifier.fillMaxSize()) {
        val horizontalPadding = 50f
        val verticalPadding = 50f

        val width = size.width - 2 * horizontalPadding
        val height = size.height - 2 * verticalPadding

        val pointDistance = if (chartData.size > 1) width / (chartData.size - 1) else width

        val path = Path()

        chartData.forEachIndexed { index, pair ->
            val x = horizontalPadding + index * pointDistance
            val y = size.height - verticalPadding - (pair.second / maxValue) * height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF3F51B5),
            style = Stroke(width = 5f)
        )

        chartData.forEachIndexed { index, pair ->
            val x = horizontalPadding + index * pointDistance
            val y = size.height - verticalPadding - (pair.second / maxValue) * height

            drawCircle(
                color = Color(0xFF3F51B5),
                center = Offset(x, y),
                radius = 8f
            )
        }

        drawLine(
            color = Color.Gray,
            start = Offset(horizontalPadding, size.height - verticalPadding),
            end = Offset(size.width - horizontalPadding, size.height - verticalPadding),
            strokeWidth = 3f
        )
        drawLine(
            color = Color.Gray,
            start = Offset(horizontalPadding, verticalPadding),
            end = Offset(horizontalPadding, size.height - verticalPadding),
            strokeWidth = 3f
        )

        chartData.forEachIndexed { index, pair ->
            val x = horizontalPadding + index * pointDistance
            val label = pair.first
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    x - 10,
                    size.height - 20,
                    android.graphics.Paint().apply {
                        color = if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                        textSize = 30f
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }
        }
    }
}