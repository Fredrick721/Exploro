package com.adorah.ExploroTravel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adorah.ExploroTravel.presentation.components.ForgotPasswordScreen
import com.adorah.ExploroTravel.presentation.components.LoginScreen
import com.adorah.ExploroTravel.presentation.components.SignUpScreen


// INSIDE THIS FILE WE WILL DEFINE NAVCONTROLLER : THIS IS USED TO NAVIGATE
// TO DIFFERENT COMPOSABLES / SCREENS
@Composable
fun ExploroTravelNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "signUp") {
        composable("signUp"){
            SignUpScreen(navController)
        }
        composable("login"){
            LoginScreen(navController)
        }

        composable("forgot password") {
            ForgotPasswordScreen(
                navController
            )
        }




    }

}
