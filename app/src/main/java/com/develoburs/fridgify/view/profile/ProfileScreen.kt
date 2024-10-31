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
                .height(200.dp)
                .background(Color.Blue, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(50.dp) // Adjust size as needed
                        .background(Color.Gray, shape = CircleShape) // Placeholder for profile picture
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Name and Numbers
                Column(
                    modifier = Modifier.weight(1f), // Take remaining space
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Yasin İBİŞ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "14", // First number
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "25", // Second number
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
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
                        // Handle click event for the recipe card
                    }
                )
            }
        }
    }
}
