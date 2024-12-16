package com.develoburs.fridgify.view.fridge

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.FridgeViewModel
import com.develoburs.fridgify.viewmodel.LoginViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(navController: NavController, viewModel: FridgeViewModel = viewModel()) {

    val allFoods by viewModel.food.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf(
        "Produce",
        "Dairy And Eggs",
        "Meats And Proteins",
        "Baking And Pantry",
        "Canned And Preserved Foods",
        "Beverages And Sweeteners",
        "Nuts Seeds And Legumes",
        "Grains And Cereals"
    )

    // Check if recipes are empty and trigger fetching them
    if (allFoods.isEmpty()) {
        viewModel.getFoodList() // Call the method to fetch recipes
    }

    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.Name?.contains(searchQuery, ignoreCase = true) == true }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.fridge),
                        color = BlackColor,
                        style = MaterialTheme.typography.labelMedium
                    )
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CreamColor2)
                )

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 21.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFFFE4B5) // Light orange color

                        )

                    )

                    // Add clickable labels below the search bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .horizontalScroll(rememberScrollState()), // Enable horizontal scrolling
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between labels
                    ) {
                        categories.forEach { category ->
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(40.dp)
                                    .background(color = OrangeColor, shape = MaterialTheme.shapes.medium)
                                    .clickable {
                                        viewModel.getFoodByCategory(category) // Fetch food for this category
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    color = BlackColor,
                                    style = MaterialTheme.typography.bodyMedium ,
                                    modifier = Modifier.padding(start = 8.dp,end= 8.dp) ,// Add right padding


                                )
                            }
                        }
                    }


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
                            item {
                                AddFoodCard(onClick = { navController.navigate("addingScreen") })
                            }
                            item {
                                DeleteFoodCard(onClick = { navController.navigate("deleteScreen") })
                            }
                        }
                    }
                }
            }
        },
    )
}
