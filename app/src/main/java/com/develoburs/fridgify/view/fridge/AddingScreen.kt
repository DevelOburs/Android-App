package com.develoburs.fridgify.view.fridge

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingScreen(navController: NavController, viewModel: FridgeViewModel = viewModel() ,onBack: () -> Unit) {

    val allFoods by viewModel.notInFridgeFood.collectAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) }

    var searchQuery by remember { mutableStateOf("") }
    val selectedItems = remember { mutableStateListOf<Food>() }
    val categories = listOf(
        "All", "Produce", "Dairy and Eggs", "Meat and Proteins",
        "Baking and Pantry", "Canned and Preserved Foods",
        "Beverages and Sweeteners", "Nuts Seeds and Legumes", "Grains and Cereals"
    )
    var displaySelectedItems by remember { mutableStateOf("") }
    if (allFoods.isEmpty()) {
        viewModel.getNotInFridgeFood()
        Log.d("NotInFridgeFoodList", allFoods.toString())
    }
    fun formatCategoryForApi(category: String): String {
        return category.uppercase().replace(" ", "_")
    }

    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.Name.contains(searchQuery, ignoreCase = true) }
    }
    LaunchedEffect(Unit) {
        isLoading = true
        viewModel.getNotInFridgeFood()
        delay(2000)
        isLoading = false
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Adding screen",
                        color = BlackColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BlackColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.height(50.dp)
            )
        },
        content = { paddingValues ->
            Surface(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeColor)
                    }
                }else {
                Image(
                    painter = painterResource(id = R.drawable.background_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
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
                    categories.forEach { category ->
                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .background(color = OrangeColor, shape = MaterialTheme.shapes.medium)
                                .clickable {
                                    val formattedCategory = if (category == "All") null else formatCategoryForApi(category)
                                    viewModel.getNotInFridgeFood(formattedCategory)

                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category,
                                color = BlackColor,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

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
                        Button(onClick = { navController.popBackStack() },modifier = Modifier.height(40.dp)) {
                            Text(text = "Exit", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(onClick = {
                            val ingredientIds = selectedItems.mapNotNull { it.id }

                            Log.d("AddingScreen", "Ingredient IDs: $ingredientIds")

                            viewModel.addFood(ingredientIds)
                            viewModel.getNotInFridgeFood()

                            displaySelectedItems = selectedItems.joinToString(", ") { it.Name }
                            selectedItems.clear()

                            Log.d("AddingScreen", "Sent ingredient IDs: $ingredientIds")

                            navController.popBackStack()

                        },modifier = Modifier.height(40.dp)
                        ) {
                            Text(text = "Add", style = MaterialTheme.typography.labelMedium)
                        }


                    }


                }
            }
        }}
    )


}