package com.adorah.ExploroTravel.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(navController: NavController, booking: Booking) {
    val scope = rememberCoroutineScope()
    var phoneNumber by remember { mutableStateOf("") }
    var paymentStatus by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Pay for ${booking.destination} Trip", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val checkoutId = mpesaService.initiatePayment(
                            phoneNumber,
                            booking.price,
                            "https://your-callback-url.com" // Replace with your server callback
                        )
                        paymentStatus = "Payment initiated. Check your phone."
                    } catch (e: Exception) {
                        paymentStatus = "Payment failed: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pay Now")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(paymentStatus, color = if (paymentStatus.contains("failed", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
    }
}