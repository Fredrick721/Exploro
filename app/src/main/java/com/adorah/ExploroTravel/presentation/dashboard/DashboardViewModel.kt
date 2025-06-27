package com.adorah.ExploroTravel.presentation.dashboard


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorah.ExploroTravel.data.repository.ExploroRepository
import com.adorah.ExploroTravel.models.ExploroItem

import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



class DashboardViewModel (
    private val repository: ExploroRepository
) : ViewModel() {

    val destination: StateFlow<List<ExploroItem>> = repository.getAllDestination()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // GETTING OUR TODOS FROM FIREBASE
    val firebaseDestination: StateFlow<List<ExploroItem>>
    = repository.fetchdestinationFromFirebase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    // expose the update and delete
    fun deleteExploroFromFirebase(ExploroTravel: ExploroItem){
        viewModelScope.launch {
            repository.deleteExploroFirebase(ExploroTravel)
        }
    }
    fun updateExploroFromFirebase(ExploroTravel: ExploroItem){
        viewModelScope.launch {
            repository.updateExploroFirebase(ExploroTravel)
        }
    }
    // [ {} , {} , { }  ]
    // functions working on the data being observed
    // 1 ....... 10000000000000000000000000000000000000000000000000000
    fun toogleExploroCompletion(todoId: Int){

        viewModelScope.launch {
            val Exploro = repository.getExploroById(Int) ?: return@launch
            val updateExploro = Exploro.copy(isCompleted = !Exploro.isCompleted)
            repository.updateExploroFirebase(exploro = Exploro)
        }
    }

    // function to add data
    fun addExploroTravel (title: String, description: String,
                  imageUri: Uri?){
        viewModelScope.launch {
            var imageUrl: String? = null
            if(imageUri != null){
              //  imageUrl = repository.uploadImagetoFirebase(imageUri)
            }
            // we create the new Item
            val newExploro = ExploroItem(
                id = 0, title = title,
                description = description, imageUri = imageUrl,
                 isCompleted = false
            )
            // room db insert
            repository.insertExploro(newExploro)
            //firebase insert
            repository.uploadToFirebase(newExploro)
        }
    }

    fun sendPasswordReset(email: String, onSuccess: () -> Unit,
                          onError: (String) -> Unit){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "An error occurred")
                }
            }
    }
}








