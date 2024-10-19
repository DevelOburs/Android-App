package com.develoburs.fridgify.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import com.develoburs.fridgify.view.fridge.FridgeScreen
import com.develoburs.fridgify.view.home.HomeScreen
import com.develoburs.fridgify.view.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = BottomBarScreen.Home.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(BottomBarScreen.Fridge.route) {
            FridgeScreen(
//                navController = navController,
            )
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen(
//                navController = navController,
            )
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(
//                navController = navController
            )
        }
    }
}