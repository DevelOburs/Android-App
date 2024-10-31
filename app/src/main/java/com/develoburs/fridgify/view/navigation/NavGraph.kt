package com.develoburs.fridgify.view.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import com.develoburs.fridgify.view.fridge.FridgeScreen
import com.develoburs.fridgify.view.home.HomeScreen
import com.develoburs.fridgify.view.profile.ProfileScreen
import com.develoburs.fridgify.view.home.RecipeDetailsScreen
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = BottomBarScreen.Home.route
) {
    // Get the ViewModel instance (use hiltViewModel() if using Hilt)
    val recipeListViewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModelFactory(navController))


    NavHost(navController = navController, startDestination = startDestination) {
        composable(BottomBarScreen.Fridge.route) {
            FridgeScreen()
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            "recipeDetails/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            // Fetch the recipeId from the arguments
            val recipeId = backStackEntry.arguments?.getString("recipeId")

            // Use the ViewModel to get the recipe by ID
            val recipe = recipeListViewModel.getRecipeById(recipeId)

            // Call RecipeDetailsScreen with the retrieved recipe
            RecipeDetailsScreen(
                recipe = recipe ?: return@composable,  // Handle null case
                onBack = { navController.popBackStack() }
            )
        }
    }
}
