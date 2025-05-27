package com.example.powerfit.view

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.powerfit.R
import com.example.powerfit.controller.Env
import com.example.powerfit.model.UserSessionViewModel
import com.example.powerfit.ui.theme.BottomMenu
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProfilePhotoScreen(navController: NavController, viewModel: UserSessionViewModel) {
    val user by viewModel.user
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            val tempFile = copyUriToFile(context, it)
            if (tempFile != null) {
                val fileUri = Uri.fromFile(tempFile)

                viewModel.uploadProfileImage(context, fileUri, Env.imgurClientId) { success, message ->
                    if (success) {
                        Toast.makeText(context, message ?: "Sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Erro: $message", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(context, "Erro ao processar imagem", Toast.LENGTH_LONG).show()
            }
        }
    }

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
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text("Alterar foto de perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            user?.let {
                val painter = rememberAsyncImagePainter(it.profileImage)

                Image(
                    painter = painter,
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(8.dp)
                )
            }


            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escolher da galeria")
            }
        }

        BottomMenu(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter),
            viewModel = viewModel
        )
    }
}

private fun copyUriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("profile_", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
