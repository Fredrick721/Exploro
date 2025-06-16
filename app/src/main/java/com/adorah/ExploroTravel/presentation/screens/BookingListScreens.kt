package com.adorah.ExploroTravel.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import com.adorah.ExploroTravel.presentation.Dashboard.BookingViewModel


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BookingListScreen(viewModel: BookingViewModel, navController: NavController) {
    val bookings by viewModel.bookings.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    LaunchedEffect(Unit) {
        viewModel.fetchBookings(userId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_booking") }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(bookings) { booking ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { navController.navigate("edit_booking/${booking.id}") }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Destination: ${booking.destination}")
                        Text("Price: $${booking.price}")
                        Text("Travel Date: ${booking.travelDate}")
                        Button(onClick = { viewModel.deleteBooking(booking.id, userId) }) {
                            Text("Delete")
                        }
                        Button(onClick = { navController.navigate("payment/${booking.id}") }) {
                            Text("Pay Now")
                        }
                    }
                }
            }
        }
    }
}