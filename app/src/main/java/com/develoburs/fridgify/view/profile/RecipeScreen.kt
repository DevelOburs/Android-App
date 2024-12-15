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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.develoburs.fridgify.ui.theme.CharcoalColor

@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = viewModel(),
    recipeType: String  // Either "liked" or "saved"
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val recipes = when (recipeType) {
        "liked" -> viewModel.userlikedrecipe.collectAsState(initial = emptyList()).value
        "saved" -> viewModel.usersavedrecipe.collectAsState(initial = emptyList()).value
        else -> viewModel.userlikedrecipe.collectAsState(initial = emptyList()).value
    }

    if (recipes.isEmpty()) {
        when (recipeType) {
            "liked" -> viewModel.getUserLikedRecipesList()
            "saved" -> viewModel.getUserSavedRecipesList()
            else -> viewModel.getUserLikedRecipesList()
        }
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
        if (recipes.isEmpty()) {
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(recipes) { index, recipe ->  // Use the list of recipes
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            navController.navigate("recipeDetails/${recipe.id}")
                        },
                        onEditClick = {
                            navController.navigate("editRecipe/${recipe.id}")
                        },
                        isProfileScreen = false
                    )
                }
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .height(36.dp)
                                    .fillMaxWidth(0.1f),
                                strokeWidth = 5.dp,
                                color = CharcoalColor
                            )
                        }

                    }
                }
            }
        }
    }
}
