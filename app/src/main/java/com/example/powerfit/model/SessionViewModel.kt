package com.example.powerfit.model

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import com.example.powerfit.controller.imgur.ImgurClient
import com.example.powerfit.controller.imgur.ImgurResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserSessionViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> get() = _user

    suspend fun loadUser(): Boolean {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return false

        return try {
            val documentSnapshot = Firebase.firestore
                .collection("usuarios")
                .document(uid)
                .get()
                .await()

            val user = documentSnapshot.toObject(User::class.java)
            _user.value = user
            user?.role == Role.TEACHER
        } catch (e: Exception) {
            false
        }
    }

    fun isTeacher(): Boolean {
        return _user.value?.role == Role.TEACHER
    }

    fun clearUser() {
        _user.value = null
    }

    fun changePassword(newPassword: String, onResult: (Boolean, String?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun changeEmail(newEmail: String, onResult: (Boolean, String?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.verifyBeforeUpdateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        FirebaseFirestore.getInstance()
                            .collection("usuarios")
                            .document(uid)
                            .update("email", newEmail)
                    }

                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun reauthenticate(currentPassword: String, onResult: (Boolean, String?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "Usuário não autenticado.")
        }
    }

    fun uploadProfileImage(
        context: Context,
        imageUri: Uri,
        clientId: String,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onResult(false, "Usuário não autenticado")
            return
        }

        (context as? ComponentActivity)?.lifecycleScope?.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val imageBytes = inputStream?.readBytes()
                if (imageBytes == null) {
                    onResult(false, "Erro ao ler a imagem")
                    return@launch
                }

                val requestFile = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData("image", "upload.jpg", requestFile)

                // Chama a API do Imgur
                val response = ImgurClient.api.uploadImage("Client-ID $clientId", multipartBody)

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link
                    if (imageUrl == null) {
                        onResult(false, "URL da imagem não retornada")
                        return@launch
                    }

                    // Atualiza o Firestore com a URL da imagem no perfil
                    Firebase.firestore.collection("usuarios")
                        .document(userId)
                        .update("profileImage", imageUrl)
                        .addOnSuccessListener {
                            _user.value = _user.value?.copy(profileImage = imageUrl)
                            onResult(true, "Imagem enviada com sucesso!")
                        }
                        .addOnFailureListener { e ->
                            onResult(false, "Erro ao salvar imagem no Firestore: ${e.message}")
                        }

                } else {
                    val errorBody = response.errorBody()?.string()
                    onResult(false, "Erro no upload do Imgur: $errorBody")
                }

            } catch (e: Exception) {
                onResult(false, "Exceção: ${e.message}")
            }
        }
    }
}