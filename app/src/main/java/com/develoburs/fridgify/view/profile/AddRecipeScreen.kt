package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.MaterialTheme
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
import com.develoburs.fridgify.viewmodel.FridgeViewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = viewModel(),
    fviewModel: FridgeViewModel = viewModel(),
    onSave: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }
    var instructions by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    val author by remember { mutableStateOf("Unknown Author") }
    val likes by remember { mutableStateOf(0) }
    val comments by remember { mutableStateOf(listOf<String>()) }

    val allFoods by fviewModel.food.collectAsState(initial = emptyList())

    val filteredFoods = remember("", allFoods) { // TEST FILTER
        allFoods.filter { it.Name.contains("", ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Add Recipe", style = MaterialTheme.typography.labelMedium) },
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
                        .padding(bottom = 72.dp) // Add padding to make space for the button
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri ?: R.drawable.background_image),
                        contentDescription = "Recipe Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(200.dp)
                            .padding(8.dp)
                            .clickable {
                                // Simulate image selection
                                imageUri = "newImageUri"
                            },
                        contentScale = ContentScale.Crop
                    )
                    // Recipe Name Section
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(text = "Recipe Name", style = MaterialTheme.typography.titleMedium) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Instructions Section
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text(text = "Instructions", style = MaterialTheme.typography.titleMedium) },
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
                            items(filteredFoods) { recipe ->
                                FoodCard(
                                    food = recipe,
                                    onClick = {},
                                )
                            }
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
                        val newRecipe = Recipe(
                            id = System.currentTimeMillis().toInt(),
                            Name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            Image = listOfNotNull(imageUri).toString(),
                            Author = author,
                            Likes = likes,
                            Comments = comments.size,
                            comments = comments
                        )
                        onSave(newRecipe)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Aligns the button to the bottom center
                        .padding(16.dp) // Add padding for aesthetics
                ) {
                    Text(text = "Save Recipe", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    )
}
