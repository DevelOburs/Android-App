package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.develoburs.fridgify.R

@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: RecipeListViewModel = viewModel(factory = RecipeListViewModelFactory(navController))
    val recipes = viewModel.recipe.collectAsState().value // Get the list of recipes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Upper Box with Name, Two Numbers, and Profile Picture
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Blue, shape = RoundedCornerShape(8.dp)),
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
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Gray, shape = CircleShape) // Placeholder for profile picture
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Name
                    Text(
                        text = "Yasin İBİŞ",
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
                                painter = painterResource(id = R.drawable.comment_icon),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                text = "10",
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
                                text = "10",
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
                        // Navigation action here, e.g., navController.navigate("settings")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.bodyLarge,  // Use a larger typography style
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
            items(recipes) { recipe ->  // Use the list of recipes
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        navController.navigate("recipeDetails/${recipe.id}")
                        // Handle click event for the recipe card
                    }
                )
            }
        }
    }
}
