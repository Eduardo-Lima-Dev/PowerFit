package com.example.powerfit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

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
}
