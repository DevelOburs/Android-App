package com.develoburs.fridgify.view.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import com.develoburs.fridgify.view.fridge.FridgeScreen
import com.develoburs.fridgify.view.fridge.AddingScreen
import com.develoburs.fridgify.view.home.HomeScreen
import com.develoburs.fridgify.view.profile.ProfileScreen
import com.develoburs.fridgify.view.profile.SettingsScreen
import com.develoburs.fridgify.view.profile.RecipeScreen
import com.develoburs.fridgify.view.home.RecipeDetailsScreen
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = BottomBarScreen.Home.route
) {
    val recipeListViewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModelFactory(navController))


    NavHost(navController = navController, startDestination = startDestination) {
        composable(BottomBarScreen.Fridge.route) {
            FridgeScreen(navController = navController)
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable("AddingScreen") {
            AddingScreen(navController = navController)
        }
        composable(
            "recipeDetails/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            // Fetch the recipeId from the arguments
            val recipeId = backStackEntry.arguments?.getString("recipeId")

            val recipe = recipeListViewModel.getRecipeById(recipeId)

            RecipeDetailsScreen(
                recipe = recipe ?: return@composable,
                onBack = { navController.popBackStack() }
            )
        }

        composable("SettingsScreen") {
            SettingsScreen(navController = navController)
        }
        composable(
            "recipes/{recipeType}",
            arguments = listOf(navArgument("recipeType") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val recipeType = backStackEntry.arguments?.getString("recipeType") ?: "liked"

            RecipeScreen(
                navController = navController,
                recipeType = recipeType)
        }
    }
}
