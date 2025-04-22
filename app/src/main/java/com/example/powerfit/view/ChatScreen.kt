package com.example.powerfit.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.powerfit.ui.theme.BottomMenu
import com.example.powerfit.model.MockAuth
import com.example.powerfit.ui.theme.ChatBubble

@Composable
fun ChatScreen(navController: NavController) {
    // Redirecionar para login caso não esteja logado
    if (!MockAuth.isLoggedIn()) {
        navController.navigate("login") {
            popUpTo(0) // Limpa toda a pilha de navegação
        }
    }

    // Classe para representar mensagens com origem
    data class ChatMessage(
        val content: String,
        val isFromUser: Boolean
    )

    // Lista de mensagens com sua origem (usuário ou bot)
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Quantas repetições na rosca direta?", true),
            ChatMessage("3x10!", false),
            ChatMessage("Qual exercício eu posso trocar?", true),
            ChatMessage("Smith", false)
        )
    }

    // Estado para o campo de texto
    var currentInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        // Botão de voltar
        IconButton(
            onClick = { navController.popBackStack() }, // Usar popBackStack em vez de navigate
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }

        // Corpo principal do chat
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 48.dp)
        ) {
            // Usando Column + verticalScroll para permitir rolagem das mensagens
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Exibe cada mensagem em uma "bolha"
                messages.forEach { msg ->
                    ChatBubble(
                        message = msg.content,
                        isUserMessage = msg.isFromUser
                    )
                }
            }

            // Campo de texto + botão de enviar
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = currentInput,
                    onValueChange = { currentInput = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Digite sua mensagem") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botão de enviar
                IconButton(onClick = {
                    if (currentInput.isNotBlank()) {
                        // Adicionar mensagem do usuário
                        messages.add(ChatMessage(currentInput, true))

                        // Simular resposta do bot (em uma aplicação real, viria de uma API)
                        val botResponse = "Resposta para: $currentInput"
                        messages.add(ChatMessage(botResponse, false))

                        currentInput = ""
                    }
                }) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(navController = rememberNavController())
}
