package com.develoburs.fridgify.view.fridge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.develoburs.fridgify.viewmodel.FridgeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavController
import com.develoburs.fridgify.model.Food

@Composable
fun AddingScreen(navController: NavController) {
    val viewModel: FridgeViewModel = viewModel(factory = viewModelFactory {
        initializer {
            FridgeViewModel()
        }
    })

    val allFoods by viewModel.food.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val selectedItems = remember { mutableStateListOf<Food>() }
    var displaySelectedItems by remember { mutableStateOf("") }

    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFoods) { food ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = if (selectedItems.contains(food)) Color.Green else Color.Transparent,
                                shape = RectangleShape
                            )
                            .clickable {
                                if (selectedItems.contains(food)) {
                                    selectedItems.remove(food)
                                } else {
                                    selectedItems.add(food)
                                }
                            }
                    ) {
                        FoodCard(
                            food = food,
                            onClick = {
                                if (selectedItems.contains(food)) {
                                    selectedItems.remove(food)
                                } else {
                                    selectedItems.add(food)
                                }
                            }
                        )



                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Exit")
            }
            Button(onClick = {
                displaySelectedItems = selectedItems.joinToString(", ") { it.name }
                //navController.popBackStack()
            }) {
                Text("Add")
            }
        }
        if (displaySelectedItems.isNotEmpty()) {
            Text(
                text = "Selected Items: $displaySelectedItems",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.Black
            )
        }
    }
}
