package com.adorah.ExploroTravel.presentation.screens.AddEditingBooking


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adorah.ExploroTravel.presentation.Dashboard.Booking
import com.adorah.ExploroTravel.presentation.Dashboard.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddEditBookingScreen(viewModel: BookingViewModel, navController: NavController, bookingId: String? = null) {
    var destination by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var travelDate by remember { mutableStateOf("") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val operationState by viewModel.operationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            if (bookingId == null) "Add Booking" else "Edit Booking",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = travelDate,
            onValueChange = { travelDate = it },
            label = { Text("Travel Date (e.g., 2025-06-15)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val booking = Booking(
                    id = bookingId ?: "",
                    destination = "",
                    price = price.toDoubleOrNull() ?: 0.0,
                    travelDate = "",
                    userId = userId
                )
                if (bookingId == null) {
                    viewModel.addBooking(booking)
                } else {
                    viewModel.updateBooking(booking)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (bookingId == null) "Add Booking" else "Update Booking")
        }

        when (val state = operationState) {
            is BookingViewModel.OperationState.Success -> {
                LaunchedEffect(state) {
                    navController.popBackStack()
                }
            }

            is BookingViewModel.OperationState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }

            is BookingViewModel.OperationState.Loading -> CircularProgressIndicator()
            else -> {}
        }
    }

}

