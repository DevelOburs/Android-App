package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.view.home.RecipeCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme

@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = viewModel(),
    recipeType: String  // Either "liked" or "saved"
) {

    // Collect the appropriate list based on recipeType
    val recipes = when (recipeType) {
        "liked" -> viewModel.recipe.collectAsState(initial = emptyList())
        "saved" -> viewModel.recipe.collectAsState(initial = emptyList())
        else -> viewModel.recipe.collectAsState(initial = emptyList())
    }

    Column {
        // Back button at the top of the screen
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Show a message if the list is empty
        if (recipes.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No recipes found for $recipeType.", style = MaterialTheme.typography.labelMedium)
            }
        } else {
            // Display the list of recipes
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(recipes.value) { index, recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            navController.navigate("recipeDetails/${recipe.id}")
                        }
                    )
                }
            }
        }
    }
}
