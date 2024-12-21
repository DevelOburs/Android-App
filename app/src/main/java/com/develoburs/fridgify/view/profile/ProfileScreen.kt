package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.develoburs.fridgify.view.home.RecipeCard
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModelFactory
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.DarkOrangeColor
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.ui.theme.MintColor
import com.develoburs.fridgify.ui.theme.OrangeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: RecipeListViewModel = viewModel(), repository: FridgifyRepositoryImpl) {
    val allRecipes = viewModel.userrecipe.collectAsState(initial = emptyList())
    // Check if recipes are empty and trigger fetching them
    val isLoading by viewModel.isLoading.collectAsState()
    if (allRecipes.value.isEmpty()) {
        with(viewModel) { getUserRecipesList() }
    }
    val userRecipeCount by viewModel.userRecipeCount.collectAsState(initial = null)
    val userLikeCount by viewModel.userLikeCount.collectAsState(initial = null)

    if (userRecipeCount == null) {
        viewModel.getUserRecipeCount()
    }
    if (userLikeCount == null) {
        viewModel.getUserLikeCount()
    }

    val recipes = allRecipes.value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.height(50.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addRecipe") }, // Navigate to Add Recipe Screen
                shape = CircleShape,
                containerColor = DarkOrangeColor,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.Add, // Replace with your "add" icon resource
                    contentDescription = "Add Recipe"
                )
            }
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CreamColor2) // Replace Color.Blue with your desired color
                )

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 21.dp)
                ) {
                    // Upper Box with Name, Two Numbers, and Profile Picture
                    // Updated Upper Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 10.dp)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        LightOrangeColor, // Deep
                                        LightOrangeColor  // Light
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Name
                                Text(
                                    text = repository.getUserFirstName() +" " + repository.getUserLastName(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // Icons and Numbers in Columns
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.SpaceEvenly,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.menu_book),
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Text(
                                            text = userRecipeCount.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.SpaceEvenly,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.favorite_icon),
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Text(
                                            text = userLikeCount.toString() ,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // Settings Button
                            IconButton(
                                onClick = {
                                    navController.navigate("SettingsScreen")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Settings",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Vertical Sliding Recipe Cards
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
                                isProfileScreen = true
                            )
                            if (index == viewModel.recipe.collectAsState().value.lastIndex) {
                                viewModel.getRecipesList()
                            }
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
        },
    )
}
