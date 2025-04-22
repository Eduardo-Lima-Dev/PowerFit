@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.powerfit.view.Student

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.powerfit.controller.ChartController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.model.MockAuth

@Preview(showBackground = true)
@Composable
fun PreviewChartScreen() {
    val navController = rememberNavController()
    ChartScreen(navController = navController)
}

@Composable
fun ChartScreen(navController: NavController) {
    // Redirecionar para login caso não esteja logado
    if (!MockAuth.isLoggedIn()) {
        navController.navigate("login") {
            popUpTo(0) // Limpa toda a pilha de navegação
        }
    }

    val chartController = ChartController()
    val chartData = chartController.getChartData()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dias Ativos por Exercícios") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomMenu(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Visão Geral dos Últimos 7 Dias",
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
                SimpleLineChart(chartData = chartData)
            }
        }
    }
}

@Composable
fun SimpleLineChart(chartData: List<Pair<String, Float>>) {
    val isDarkTheme = isSystemInDarkTheme()
    val maxValue = chartData.maxOf { it.second }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val horizontalPadding = 50f
        val verticalPadding = 50f

        val width = size.width - 2 * horizontalPadding
        val height = size.height - 2 * verticalPadding

        val pointDistance = width / (chartData.size - 1)

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