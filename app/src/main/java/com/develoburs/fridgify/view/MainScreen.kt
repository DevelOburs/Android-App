package com.develoburs.fridgify.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.develoburs.fridgify.ui.theme.BlueColor
import com.develoburs.fridgify.view.bottombar.BottomBar
import com.develoburs.fridgify.view.navigation.NavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Get the current back stack entry to determine the current destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Check if the current destination is login or register
    val showBottomBar = currentDestination?.route !in listOf("login", "register")

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { paddingValues ->
        // Wrap the NavGraph inside a Box and apply padding
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            ) {
            NavGraph(navController = navController)
        }
    }
}
