package com.example.powerfit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Student(
    val id: String = "",
    val name: String = "",
    val age: Int = 0,
    val trains: Boolean = false,
    val hasComorbidity: Boolean = false,
    val weight: Float = 0f
) {
    constructor() : this("", "", 0, false, false, 0f)
}

class StudentViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _registeredStudents = mutableStateOf<List<Student>>(emptyList())
    val registeredStudents: State<List<Student>> = _registeredStudents

    private val _vinculatedStudents = mutableStateOf<List<Student>>(emptyList())
    val vinculatedStudents: State<List<Student>> = _vinculatedStudents

    private val _usersWithRoleUser = mutableStateOf<List<Student>>(emptyList())
    val usersWithRoleUser: State<List<Student>> = _usersWithRoleUser

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadStudents()
        loadUsersWithRoleUser()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val registeredSnapshot = db.collection("registeredStudents").get().await()
                _registeredStudents.value = registeredSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Student::class.java)?.copy(id = doc.id)
                }

                val vinculatedSnapshot = db.collection("vinculatedStudents").get().await()
                _vinculatedStudents.value = vinculatedSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Student::class.java)?.copy(id = doc.id)
                }
            } catch (e: Exception) {
                // Handle errors
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUsersWithRoleUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val usersSnapshot = db.collection("usuarios")
                    .whereEqualTo("role", "USER")
                    .get()
                    .await()

                _usersWithRoleUser.value = usersSnapshot.documents.mapNotNull { doc ->
                    val userData = doc.data
                    userData?.let {
                        Student(
                            id = doc.id,
                            name = it["name"] as? String ?: "",
                            age = (it["age"] as? Long)?.toInt() ?: 0,
                            trains = false,
                            hasComorbidity = false,
                            weight = 0f
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle errors
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateVinculatedStudent(updated: Student) {
        viewModelScope.launch {
            try {
                db.collection("vinculatedStudents").document(updated.id).set(updated).await()
                loadStudents()
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun updateRegisteredStudent(updated: Student) {
        viewModelScope.launch {
            try {
                db.collection("registeredStudents").document(updated.id).set(updated).await()
                loadStudents()
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    fun vinculateStudent(student: Student) {
        viewModelScope.launch {
            try {
                db.collection("registeredStudents").document(student.id).delete().await()
                db.collection("vinculatedStudents").document(student.id).set(student).await()
                db.collection("usuarios").document(student.id)
                    .update(mapOf(
                        "vinculado" to true,
                        "teacherId" to getCurrentUserId()
                    ))
                    .await()
                loadStudents()
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}