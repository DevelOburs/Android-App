package com.develoburs.fridgify.view.fridge

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.DarkOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.FridgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(navController: NavController, viewModel: FridgeViewModel = viewModel(),onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val isLoading by viewModel.isLoading.collectAsState()

    fun formatCategoryForApi(category: String): String {
        return category.uppercase().replace(" ", "_")
    }

    LaunchedEffect(Unit) {
        viewModel.getFoodList()
    }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            viewModel.getFoodList()
        }
    }
    val allFoods by viewModel.food.collectAsState(initial = emptyList())


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.fridge),
                        color = CharcoalColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent

                ),
                modifier = Modifier
                    .height(50.dp)
                    .background(color = CreamColor2)
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = CreamColor2)
            ) {
                if (isLoading) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = CreamColor2),
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
                } else {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .background(color = CreamColor2)
                            .padding(top = 21.dp)
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFFFE4B5)
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val categories = listOf(
                                "All",
                                "Produce",
                                "Dairy and Eggs",
                                "Meat and Proteins",
                                "Baking and Pantry",
                                "Canned and Preserved Foods",
                                "Beverages and Sweeteners",
                                "Nuts Seeds and Legumes",
                                "Grains and Cereals"
                            )
                            categories.forEach { category ->
                                val isSelected = selectedCategory == category
                                Box(
                                    modifier = Modifier
                                        .height(35.dp)
                                        .background(
                                            color = if (isSelected) DarkOrangeColor else OrangeColor,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            selectedCategory = category
                                            val formattedCategory = if (category == "All") null else formatCategoryForApi(category)
                                            viewModel.getFoodList(formattedCategory)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category,
                                        color = if (isSelected) BlackColor else Color.White,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(allFoods.filter { it.Name.contains(searchQuery, ignoreCase = true) }) { food ->
                                FoodCard(food = food, onClick = {})
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
        }
    )
}
