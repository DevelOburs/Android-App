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
import com.develoburs.fridgify.viewmodel.FridgeViewModel
import com.develoburs.fridgify.viewmodel.FridgeViewModelFactory
import com.develoburs.fridgify.viewmodel.LoginViewModel
import com.develoburs.fridgify.viewmodel.LoginViewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf



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

    val fridgeViewModel: FridgeViewModel = viewModel(factory = FridgeViewModelFactory(repository))

    // Retrieve the user token
    val userToken: String = repository.getToken() // Use the getToken function from your repository
    // Here, you can define your user token (for now, assume it's always present)

    NavHost(navController = navController, startDestination = if (userToken == "") "login" else startDestination) {
        // Define composable screens with checks for user token
        composable(BottomBarScreen.Fridge.route) {
            FridgeScreen(navController = navController, viewModel = fridgeViewModel)
        }
        composable(BottomBarScreen.Home.route) {
            HomeScreen(navController = navController, viewModel = recipeListViewModel)
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = recipeListViewModel)
        }
        composable("AddingScreen") {
            AddingScreen(navController = navController, viewModel = fridgeViewModel, onBack = { navController.popBackStack() })
        }
        composable("DeleteScreen") {
            DeleteScreen(navController = navController, viewModel = fridgeViewModel, onBack = { navController.popBackStack() })
        }
        composable(
            route = "recipeDetails/{recipeId}"
        ) { backStackEntry ->
            // Retrieve the recipeId as a String
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: return@composable

            // Use remember to hold the loading state
            val isLoading = remember { mutableStateOf(true) }

            // Pass recipeId to the RecipeDetailsScreen and observe loading status
            Box(modifier = Modifier.fillMaxSize()) {
                RecipeDetailsScreen(
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() },
                    navController = navController,
                    repository = repository,
                    onLoadingFinished = { isLoading.value = false } // Callback to update loading state
                )

                // Display a loading indicator while data is loading
                if (isLoading.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }


        composable(
            "editRecipe/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: return@composable

            // Observe the recipe state from the ViewModel
            val recipe by recipeListViewModel.recipeDetail.collectAsState()

            // Trigger the recipe fetch when entering the composable
            LaunchedEffect(recipeId) {
                recipeListViewModel.getRecipeById(recipeId)
            }

            if (recipe != null) {
                EditRecipeScreen(
                    navController = navController,
                    recipe = recipe!!, // Pass the fetched recipe
                    onSave = { updatedRecipe ->
                        recipeListViewModel.updateRecipe(updatedRecipe)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Show a loading indicator while waiting for the recipe
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }


        composable("SettingsScreen") {
            SettingsScreen(navController = navController, viewModel = loginViewModel)
        }
        composable("addRecipe") {
            AddRecipeScreen(
                navController = navController,
                viewModel = recipeListViewModel,
                fviewModel = fridgeViewModel,
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
            RegisterPageScreen(navController = navController, viewModel = loginViewModel) // No bottom bar on this screen
        }
    }
}
