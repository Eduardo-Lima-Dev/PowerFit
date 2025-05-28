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
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class UserSessionViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    private val _uid = mutableStateOf<String?>(null)
    val user: State<User?> get() = _user
    val uid: State<String?> get() = _uid

    suspend fun loadUser(): Boolean {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return false
        return try {
            val documentSnapshot = Firebase.firestore
                .collection("usuarios")
                .document(uid)
                .get()
                .await()

            val user = documentSnapshot.toObject(User::class.java)
            _uid.value = uid
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

    fun loadWeeklyProgress(userId: String, onResult: (Map<DayOfWeek, Boolean>) -> Unit) {
        val today = LocalDate.now()
        val daysSinceSunday = (today.dayOfWeek.value % 7) // domingo = 0, segunda = 1, ..., sábado = 6
        val startOfWeek = today.minusDays(daysSinceSunday.toLong())
        val formatter = DateTimeFormatter.ISO_DATE

        Firebase.firestore.collection("user_progress")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                val progressMap = mutableMapOf<DayOfWeek, Boolean>()

                for (i in 0..6) {
                    val date = startOfWeek.plusDays(i.toLong())
                    val key = date.format(formatter)
                    val day = date.dayOfWeek
                    progressMap[day] = doc.getBoolean(key) == true
                }

                onResult(progressMap)
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

    suspend fun fetchWeeklyPresenceCounts(userId: String): Map<out Any, Int> {
        val doc = FirebaseFirestore.getInstance()
            .collection("user_progress")
            .document(userId)
            .get()
            .await()

        if (!doc.exists()) {
            return DayOfWeek.values().associateWith { 0 }
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // Inicializa mapa com zero para todos os dias
        val counts = DayOfWeek.values().associateWith { 0 }.toMutableMap()

        for ((key, value) in doc.data ?: emptyMap()) {
            // Verifica se a chave é uma data válida no formato yyyy-MM-dd
            val date = try {
                LocalDate.parse(key, formatter)
            } catch (e: Exception) {
                null
            }

            if (date != null) {
                val present = value as? Boolean ?: false
                if (present) {
                    val dayOfWeek = date.dayOfWeek
                    counts[dayOfWeek] = counts.getValue(dayOfWeek) + 1
                }
            }
        }
        val countsFormatado: Map<String, Int> = linkedMapOf(
            "Domingo" to counts.getOrDefault(DayOfWeek.SUNDAY, 0),
            "Segunda" to counts.getOrDefault(DayOfWeek.MONDAY, 0),
            "Terça" to counts.getOrDefault(DayOfWeek.TUESDAY, 0),
            "Quarta" to counts.getOrDefault(DayOfWeek.WEDNESDAY, 0),
            "Quinta" to counts.getOrDefault(DayOfWeek.THURSDAY, 0),
            "Sexta" to counts.getOrDefault(DayOfWeek.FRIDAY, 0),
            "Sábado" to counts.getOrDefault(DayOfWeek.SATURDAY, 0),
        )

        return countsFormatado
    }

    suspend fun getLinkedStudent(): StudentAssessment? {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return null

        return try {
            val snapshot = Firebase.firestore
                .collection("vinculatedStudents")
                .document(currentUid)
                .get()
                .await()

            snapshot.toObject(StudentAssessment::class.java)
        } catch (e: Exception) {
            null
        }
    }

}