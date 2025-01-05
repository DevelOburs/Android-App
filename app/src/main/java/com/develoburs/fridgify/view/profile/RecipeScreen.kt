package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.view.home.RecipeCard
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
@OptIn(ExperimentalMaterial3Api::class)
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
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = if (recipeType == "liked") "Liked Recipes" else "Saved Recipes",
                    color = CharcoalColor,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = CharcoalColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.height(60.dp)
        )

        // Show a message if the list is empty
        if (recipes.isEmpty() && !isLoading) {
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
