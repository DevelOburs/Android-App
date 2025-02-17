package com.develoburs.fridgify.view.profile

import android.util.Log

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.createRecipe
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.view.fridge.AddFoodCard
import com.develoburs.fridgify.view.fridge.FoodCard
import com.develoburs.fridgify.viewmodel.FridgeViewModel
import com.develoburs.fridgify.viewmodel.RecipeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = viewModel(),
    fviewModel: FridgeViewModel = viewModel(),
    recipe: Recipe,
    repository: FridgifyRepositoryImpl,
    onSave: (Int, createRecipe) -> Unit,
    onBack: () -> Unit,
    onDelete: (String) -> Unit,
) {
    var name by remember { mutableStateOf(recipe.Name) }
    var description by remember { mutableStateOf(recipe.instructions) }
    var calories by remember { mutableStateOf(recipe.calories) }
    var cookingTime by remember { mutableStateOf(recipe.cookingTime) }
    var imageUrl by remember { mutableStateOf(recipe.Image ?: "") }
    var category by remember { mutableStateOf(recipe.Category) }

    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val ingredients: List<Food>? = recipe.ingredients
    val selectedItems = fviewModel.selectedFoods

    LaunchedEffect(recipe.id) {
        ingredients?.forEach { ingredient ->
            // Check if the ingredient is not already in selectedItems before adding
            if (!selectedItems.any { it.id == ingredient.id }) {
                selectedItems.add(ingredient)
            }
        }
    }


    val categoryMap = mapOf(
        "APPETIZERS_AND_SNACKS" to "Appetizers and Snacks",
        "MAIN_DISHES" to "Main Dishes",
        "SIDE_DISHES" to "Side Dishes",
        "SOUPS_AND_STEWS" to "Soups and Stews",
        "BREADS_AND_BAKING" to "Breads and Baking",
        "DESSERTS_AND_SWEETS" to "Desserts and Sweets",
        "BEVERAGES" to "Beverages",
        "SPECIAL_DIETS" to "Special Diets"
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Edit Recipe", style = MaterialTheme.typography.labelMedium) },
                navigationIcon = {
                    IconButton(onClick = {
                        selectedItems.clear() // Clear selected items when navigating back
                        onBack()
                    }) {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 72.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Recipe Image Section
                    item {
                        RecipeImagePicker(
                            imageUrl = imageUrl,
                            onImageSelected = { uri ->
                                // Pass the URI to ViewModel for upload
                                uri?.let { viewModel.uploadImage(it) }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Recipe Name Section
                    item {
                        name?.let {
                            OutlinedTextField(
                                value = it,
                                onValueChange = { name = it },
                                label = { Text(text = "Recipe Name", style = MaterialTheme.typography.titleMedium) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Description Section
                    item {
                        description?.let {
                            OutlinedTextField(
                                value = it,
                                onValueChange = { description = it },
                                label = { Text(text = "Description", style = MaterialTheme.typography.titleMedium) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Cooking Time Section
                    item {
                        OutlinedTextField(
                            value = cookingTime.toString(),
                            onValueChange = { cookingTime = it.toIntOrNull() ?: 0 },
                            label = { Text(text = "Cooking Time (mins)", style = MaterialTheme.typography.titleMedium) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Calories Section
                    item {
                        OutlinedTextField(
                            value = calories.toString(),
                            onValueChange = { calories = it.toIntOrNull() ?: 0 },
                            label = { Text(text = "Calories", style = MaterialTheme.typography.titleMedium) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Category Dropdown Section
                    item {
                        var expanded by remember { mutableStateOf(false) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = categoryMap[category] ?: "",
                                onValueChange = {},
                                label = { Text(text = "Category") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { expanded = true }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                categoryMap.forEach { (key, value) ->
                                    DropdownMenuItem(
                                        text = { Text(value) },
                                        onClick = {
                                            category = key
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Ingredients Section with LazyHorizontalGrid
                    item {
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height(200.dp)
                        ) {
                            items(selectedItems) { food ->
                                FoodCard(
                                    food = food,
                                    onClick = {}
                                )
                            }
                            // Add Food Card
                            item {
                                AddFoodCard(onClick = { navController.navigate("AddEditFoodScreen") })
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = com.develoburs.fridgify.ui.theme.BurgundyColor,
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.White
                            ),
                            onClick = {
                                selectedItems.clear()
                                onDelete(recipe.id)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Delete Recipe", style = MaterialTheme.typography.bodyLarge)
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = com.develoburs.fridgify.ui.theme.BrightGreenColor,
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.White
                            ),
                            onClick = {
                                if (name?.isBlank() == true) {
                                    errorMessage = "Recipe name cannot be empty."
                                    showDialog = true
                                } else if (selectedItems.isEmpty()) {
                                    errorMessage = "Ingredients cannot be empty."
                                    showDialog = true
                                } else {
                                val updatedRecipe = createRecipe(
                                    name = name,
                                    description = description,
                                    userId = repository.getUserID().toString(),
                                    userUsername = repository.getUserName(),
                                    userFirstName = repository.getUserName(),
                                    userLastName = repository.getUserName(),
                                    likeCount = recipe.Likes,
                                    commentCount = recipe.Comments,
                                    saveCount = recipe.saveCount,
                                    ingredients = selectedItems.toList(),
                                    imageUrl = imageUrl,
                                    category = category,
                                    calories = calories,
                                    cookingTime = cookingTime
                                )
                                Log.d("RecipeUpdate", "Updated recipe: $updatedRecipe")
                                selectedItems.clear()
                                onSave(recipe.id.toInt(), updatedRecipe)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Save Changes", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    )
    // Error Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.titleMedium,
                    color = com.develoburs.fridgify.ui.theme.BurgundyColor
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = com.develoburs.fridgify.ui.theme.OrangeColor
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.develoburs.fridgify.ui.theme.BurgundyColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        )
    }
}

