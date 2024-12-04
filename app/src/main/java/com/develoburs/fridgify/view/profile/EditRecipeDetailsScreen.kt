package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.view.fridge.AddFoodCard
import com.develoburs.fridgify.view.fridge.DeleteFoodCard
import com.develoburs.fridgify.view.fridge.FoodCard
import com.develoburs.fridgify.viewmodel.FridgeViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    navController: NavController,
    recipe: Recipe,
    onSave: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(recipe.Name) }
    var ingredients by remember { mutableStateOf(recipe.ingredients) }
    var instructions by remember { mutableStateOf(recipe.instructions) }
    var imageUri by remember { mutableStateOf(recipe.Images?.firstOrNull() ?: "") } // Use safe call and provide a default value
    // Assuming first image is the main one

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 21.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 72.dp)
                ) {
                    // Image Section: Make the image clickable
                    imageUri?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "Recipe Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(200.dp)
                                .padding(8.dp)
                                .clickable {
                                    // Replace with actual image picker logic, this is a placeholder action
                                    imageUri = "newImageUri" // Simulating image change on click
                                },
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Recipe Name Section
                    OutlinedTextField(
                        value = name ?: "", // Provide a default empty string if null
                        onValueChange = { name = if (it.isBlank()) null else it }, // Set to null if blank
                        label = { Text(text = "Recipe Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Instructions Section
                    OutlinedTextField(
                        value = instructions ?: "", // Provide a default empty string if null
                        onValueChange = { instructions = if (it.isBlank()) null else it }, // Set to null if blank
                        label = { Text(text = "Instructions") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Ingredients Section with LazyVerticalGrid
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            // Add Food Card
                            item {
                                AddFoodCard(onClick = { navController.navigate("addingScreen") })
                            }

                            // Delete Food Card
                            item {
                                DeleteFoodCard(onClick = { navController.navigate("deleteScreen") })
                            }
                        }
                    }

                }
                // Save Button positioned at the bottom
                Button(
                    onClick = {
                        val updatedRecipe = recipe.copy(
                            Name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            Images = listOfNotNull(imageUri) // Update images list if a new image is set
                        )
                        onSave(updatedRecipe)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)// Aligns the button to the bottom center
                        .padding(16.dp) // Add padding for aesthetics
                ) {
                    Text("Save Changes")
                }
            }
        }
    )
}
