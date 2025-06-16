package com.adorah.ExploroTravel.presentation.Dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Booking(
    val id: String = "",
    val destination: String,
    val price: Double,
    val travelDate: String,
    val userId: String
)

class BookingViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState: StateFlow<OperationState> = _operationState

    sealed class OperationState {
        object Idle : OperationState()
        object Loading : OperationState()
        data class Success(val message: String) : OperationState()
        data class Error(val message: String) : OperationState()
    }

    fun fetchBookings(userId: String) {
        viewModelScope.launch {
            try {
                val result = db.collection("bookings").whereEqualTo("userId", userId).get().await()
                _bookings.value = result.toObjects(Booking::class.java)
            } catch (e: Exception) {
                _operationState.value = OperationState.Error("Failed to load bookings")
            }
        }
    }

    fun addBooking(booking: Booking) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            try {
                db.collection("bookings").add(booking).await()
                _operationState.value = OperationState.Success("Booking added")
                fetchBookings(booking.userId)
            } catch (e: Exception) {
                _operationState.value = OperationState.Error("Failed to add booking")
            }
        }
    }

    fun updateBooking(booking: Booking) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            try {
                db.collection("bookings").document(booking.id).set(booking).await()
                _operationState.value = OperationState.Success("Booking updated")
                fetchBookings(booking.userId)
            } catch (e: Exception) {
                _operationState.value = OperationState.Error("Failed to update booking")
            }
        }
    }

    fun deleteBooking(bookingId: String, userId: String) {
        viewModelScope.launch {
            _operationState.value = OperationState.Loading
            try {
                if (bookingId.isEmpty()) {
                    _operationState.value = OperationState.Error("Invalid booking ID")
                    return@launch
                }
                db.collection("bookings").document(bookingId).delete().await()
                _operationState.value = OperationState.Success("Booking deleted")
                fetchBookings(userId)
            } catch (e: Exception) {
                _operationState.value = OperationState.Error("Failed to delete booking: ${e.message}")
            }
        }
    }