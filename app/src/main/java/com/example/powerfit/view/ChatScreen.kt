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
import com.example.powerfit.controller.chat.ChatRequest
import com.example.powerfit.controller.chat.ChatResponse
import com.example.powerfit.controller.chat.Message
import com.example.powerfit.controller.chat.RetrofitClient
import com.example.powerfit.ui.theme.ChatBubble
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun ChatScreen(navController: NavController) {
    // Classe para representar mensagens com origem
    data class ChatMessage(
        val content: String,
        val isFromUser: Boolean
    )

    // Lista de mensagens com sua origem (usuário ou bot)
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Olá, eu sou o PitBot. Como posso lhe ajudar?", false)
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
                        messages.add(ChatMessage(currentInput, true))

                        // Requisição à LLM via OpenRouter
                        val userMessage = Message("user", currentInput)
                        val request = ChatRequest(messages = listOf(userMessage))

                        RetrofitClient.api.sendMessage(request).enqueue(object : Callback<ChatResponse> {
                            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                                val reply = response.body()?.choices?.firstOrNull()?.message?.content
                                    ?: "Desculpe, não entendi."
                                messages.add(ChatMessage(reply, false))
                            }

                            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                                val errorMsg = "Falha na comunicação: ${t.localizedMessage}"
                                messages.add(ChatMessage(errorMsg, false))
                            }
                        })

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
