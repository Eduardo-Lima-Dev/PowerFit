package com.example.powerfit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserSessionViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> get() = _user

    fun loadUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                _user.value = doc.toObject(User::class.java)
            }
    }

    fun isTeacher(): Boolean {
        return _user.value?.role == Role.TEACHER
    }

    fun clearUser() {
        _user.value = null
    }
}
