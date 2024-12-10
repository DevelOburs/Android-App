package com.develoburs.fridgify.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: RecipeListViewModel = viewModel()) {
    val allRecipes = viewModel.recipe.collectAsState(initial = emptyList())

    var showFiltered by remember { mutableStateOf(false) }

    // Check if recipes are empty and trigger fetching them
    if (allRecipes.value.isEmpty()) {
        viewModel.getRecipesList() // Call the method to fetch recipes
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home),
                        color = BlackColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                actions = {
                    var menuExpanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu_icon),
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                showFiltered = false
                            },
                            text = { Text(stringResource(id = R.string.show_all), style = MaterialTheme.typography.titleMedium) }
                        )
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                showFiltered = true
                            },
                            text = { Text(stringResource(id = R.string.filter_by_comments), style = MaterialTheme.typography.titleMedium) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.height(50.dp)
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                val recipesToShow = if (showFiltered) {
                    allRecipes.value.filter { (it.Comments ?: 0) >= 10 }
                } else {
                    allRecipes.value
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 21.dp)
                ) {
                    itemsIndexed(recipesToShow) { index, recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                navController.navigate("recipeDetails/${recipe.id}")
                            },
                            onEditClick = {
                                navController.navigate("editRecipe/${recipe.id}") // Navigate to the edit screen
                            },
                            isProfileScreen = false
                        )
                    }
                }
            }
        },
    )
}