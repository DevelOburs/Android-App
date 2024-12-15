package com.develoburs.fridgify.view.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
<<<<<<< Updated upstream
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
=======
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
>>>>>>> Stashed changes
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.develoburs.fridgify.R
<<<<<<< Updated upstream
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
=======
import com.develoburs.fridgify.model.createRecipe
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
>>>>>>> Stashed changes
import com.develoburs.fridgify.view.fridge.AddFoodCard
import com.develoburs.fridgify.view.fridge.FoodCard
import com.develoburs.fridgify.viewmodel.FridgeViewModel
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
<<<<<<< Updated upstream
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }
    var instructions by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    val author by remember { mutableStateOf("Unknown Author") }
    val likes by remember { mutableStateOf(0) }
    val comments by remember { mutableStateOf(listOf<String>()) }
=======
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var ingredients by rememberSaveable { mutableStateOf(listOf<String>()) }
    var calories by rememberSaveable { mutableStateOf(0) }
    var cookingTime by rememberSaveable { mutableStateOf(0) }
    var imageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var category by rememberSaveable { mutableStateOf("APPETIZERS_AND_SNACKS") }

    val categoryMap = mapOf(
        "ALL" to "All",
        "APPETIZERS_AND_SNACKS" to "Appetizers and Snacks",
        "MAIN_DISHES" to "Main Dishes",
        "SIDE_DISHES" to "Side Dishes",
        "SOUPS_AND_STEWS" to "Soups and Stews",
        "BREADS_AND_BAKING" to "Breads and Baking",
        "DESSERTS_AND_SWEETS" to "Desserts and Sweets",
        "BEVERAGES" to "Beverages",
        "SPECIAL_DIETS" to "Special Diets"
    )
>>>>>>> Stashed changes

    val selectedItems = fviewModel.selectedFoods

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
                            items(selectedItems) { recipe ->
                                FoodCard(
                                    food = recipe,
                                    onClick = {},
                                )
                            }
                            // Add Food Card
                            item {
                                AddFoodCard(onClick = { navController.navigate("AddEditFoodScreen") })
                            }
                        }
                    }
                }

                // Save Button positioned at the bottom
                Button(
                    onClick = {
<<<<<<< Updated upstream
                        val newRecipe = Recipe(
                            id = System.currentTimeMillis().toString(),
                            Name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            Image = listOfNotNull(imageUri).toString(),
                            Author = author,
                            Likes = likes,
                            Comments = comments.size,
                            comments = comments
=======
                        val newRecipe = createRecipe(
                            name = name,
                            description = description,
                            userId = repository.getUserID().toString(), // Replace with actual user ID
                            userUsername = repository.getUserName(),
                            userFirstName = repository.getUserName(),
                            userLastName = repository.getUserName(),
                            likeCount = 0,
                            commentCount = 0,
                            saveCount = 0,
                            ingredients = selectedItems.map { it.Name },
                            imageUrl = imageUrl ?: "deneme",
                            category = category,
                            calories = calories,
                            cookingTime = cookingTime
>>>>>>> Stashed changes
                        )
                        // Debug log
                        Log.d("RecipeCreation", "New recipe created: $newRecipe")
                        onSave(newRecipe)
                        selectedItems.clear()
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
