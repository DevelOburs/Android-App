package com.develoburs.fridgify.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.develoburs.fridgify.model.api.AuthApi
import com.develoburs.fridgify.model.api.RetrofitInstance
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.view.bottombar.BottomBarScreen
import com.develoburs.fridgify.view.fridge.FridgeScreen
import com.develoburs.fridgify.view.fridge.AddingScreen
import com.develoburs.fridgify.view.fridge.DeleteScreen
import com.develoburs.fridgify.view.home.HomeScreen
import com.develoburs.fridgify.view.profile.ProfileScreen
import com.develoburs.fridgify.view.profile.SettingsScreen
import com.develoburs.fridgify.view.profile.RecipeScreen
import com.develoburs.fridgify.view.profile.EditRecipeScreen
import com.develoburs.fridgify.view.profile.AddRecipeScreen
import com.develoburs.fridgify.view.profile.LoginPageScreen
import com.develoburs.fridgify.view.profile.RegisterPageScreen
import com.develoburs.fridgify.view.home.RecipeDetailsScreen
import com.develoburs.fridgify.viewmodel.LoginViewModel
import com.develoburs.fridgify.viewmodel.LoginViewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = BottomBarScreen.Home.route
) {
    val repository = remember { FridgifyRepositoryImpl() }
    val recipeListViewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModelFactory(navController,repository))

    val authApi: AuthApi = remember { RetrofitInstance.authapi }


    // Create an instance of LoginViewModel
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(authApi, repository))


    // Retrieve the user token
    val userToken: String = repository.getToken() // Use the getToken function from your repository
    // Here, you can define your user token (for now, assume it's always present)

    NavHost(navController = navController, startDestination = if (userToken == "") "login" else startDestination) {
        // Define composable screens with checks for user token
        composable(BottomBarScreen.Fridge.route) {
            FridgeScreen(navController = navController)
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController = navController, viewModel = recipeListViewModel)
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = recipeListViewModel)
        }
        composable("AddingScreen") {
            AddingScreen(navController = navController, onBack = { navController.popBackStack() })
        }
        composable("DeleteScreen") {
            DeleteScreen(navController = navController, onBack = { navController.popBackStack() })
        }
        composable(
            "recipeDetails/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toInt()
            val recipe = recipeListViewModel.getRecipeById(recipeId)
            RecipeDetailsScreen(
                recipe = recipe ?: return@composable,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            "editRecipe/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            val recipe = recipeListViewModel.getRecipeById(recipeId?.toInt())

            EditRecipeScreen(
                navController = navController,
                recipe = recipe ?: return@composable,
                onSave = { updatedRecipe ->
                    recipeListViewModel.updateRecipe(updatedRecipe)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("SettingsScreen") {
            SettingsScreen(navController = navController)
        }
        composable("addRecipe") {
            AddRecipeScreen(
                navController = navController,
                onSave = { newRecipe ->
                    recipeListViewModel.addRecipe(newRecipe) // Add the new recipe to your ViewModel
                    navController.popBackStack() // Navigate back after saving
                },
                onBack = { navController.popBackStack() } // Handle back navigation
            )
        }
        composable(
            "recipes/{recipeType}",
            arguments = listOf(navArgument("recipeType") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val recipeType = backStackEntry.arguments?.getString("recipeType") ?: "liked"
            RecipeScreen(navController = navController, recipeType = recipeType)
        }
        composable("login") {
            LoginPageScreen(navController = navController, viewModel = loginViewModel) // Pass the LoginViewModel here
        }
        composable("register") {
            RegisterPageScreen(navController = navController) // No bottom bar on this screen
        }
    }
}
