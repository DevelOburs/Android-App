package com.develoburs.fridgify.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    // Initialize the ViewModel with the custom factory that takes navController as a parameter
    val viewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModelFactory(navController))

    // Collect state from the ViewModel using collectAsState
    val allRecipes = viewModel.recipe.collectAsState(initial = emptyList())

    // UI Layout
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            itemsIndexed(allRecipes.value) { index, recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        // Navigate to recipe details with the recipe ID
                        navController.navigate("recipeDetails/${recipe.id}")
                    }
                )
            }
        }
    }
}
