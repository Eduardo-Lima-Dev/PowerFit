package com.example.powerfit.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(
    message: String,
    isUserMessage: Boolean
) {
    // Alinha à direita se for mensagem do usuário, à esquerda se for mensagem do "bot"
    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    // Cor de fundo diferente conforme a origem da mensagem (usuário ou bot)
    val bubbleColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment // Define o alinhamento da bolha
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}