package com.adorah.ExploroTravel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.navigation.compose.rememberNavController
import com.adorah.ExploroTravel.presentation.navigation.ExploroTravelNavGraph
import com.adorah.ExploroTravel.ui.theme.ExploroTravelTheme

import com.google.firebase.FirebaseApp



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // initialize firebase for the app
        FirebaseApp.initializeApp(this)
        setContent {
            ExploroTravelTheme {
                val navController = rememberNavController()
                ExploroTravelNavGraph(navController)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // CUSTOM ONSTART LOGIC FOR THIS ACTIVITY
    }



}