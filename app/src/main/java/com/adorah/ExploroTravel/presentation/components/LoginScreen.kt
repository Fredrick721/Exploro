package com.adorah.ExploroTravel.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }



    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = { email = it },
                label = { Text("Email") })

            OutlinedTextField(value = password, onValueChange = { password = it},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation())

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Email and password are required"
                } else {
                    Firebase.auth.signInWithEmailAndPassword(email,
                        password).addOnCompleteListener{
                            task -> if (task.isSuccessful) {
                        navController.
                        navigate("dashboard")
                    } else {
                        error = task.exception?.message
                    }
                    }
                }

            }) {
                Text("Login")
            }
            TextButton(onClick = {
                navController.navigate("signUp")
            }) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}


