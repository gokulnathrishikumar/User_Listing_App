package com.example.listingapp.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.listingapp.ui.SplashScreen
import com.example.listingapp.ui.UserListScreen
import com.example.listingapp.ui.UserDetailsScreen
import com.example.listingapp.viewmodel.UserViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: UserViewModel) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("list") {
                    popUpTo("splash") { inclusive = true } // Remove splash from back stack
                }
            }
        }
        composable("list") {
            UserListScreen(navController, viewModel)
        }
        composable("details/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            UserDetailsScreen(navController, userId, viewModel)
        }
    }
}
