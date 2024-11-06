package com.develoburs.fridgify.view.fridge
import androidx.compose.foundation.layout.Arrangement


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.develoburs.fridgify.viewmodel.FridgeViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
@Composable
fun FridgeScreen() {
    val viewModel: FridgeViewModel = viewModel(factory = viewModelFactory {
        initializer {
            FridgeViewModel()
        }
    })


    val allFoods by viewModel.food.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }

    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search...") },
            modifier = Modifier
                .fillMaxWidth()

        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
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
            }
        }
    }
}