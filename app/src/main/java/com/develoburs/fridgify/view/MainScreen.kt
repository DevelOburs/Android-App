package com.develoburs.fridgify.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.develoburs.fridgify.ui.theme.BlueColor
import com.develoburs.fridgify.view.bottombar.BottomBar
import com.develoburs.fridgify.view.navigation.NavGraph

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize(), color = BlueColor) {

        }
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(navController = navController)
        }
    }
}