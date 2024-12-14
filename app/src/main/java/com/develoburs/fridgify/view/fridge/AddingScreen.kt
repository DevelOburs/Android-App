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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.ui.theme.BlackColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingScreen(
    navController: NavController,
    viewModel: FridgeViewModel = viewModel(),
    onBack: () -> Unit
) {

    val allFoods by viewModel.notInFridgeFood.collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }
    val selectedItems = remember { mutableStateListOf<Food>() }
    var displaySelectedItems by remember { mutableStateOf("") }
    if (allFoods.isEmpty()) {
        viewModel.getNotInFridgeFood() // Call the method to fetch recipes
        Log.d("NotInFridgeFoodList", allFoods.toString())
    }

    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.Name.contains(searchQuery, ignoreCase = true) }
    }
    var showSnackbar by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Adding screen", //TODO will be changed later
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
                        placeholder = {
                            Text(
                                text = "Search",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
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
                            Text(text = "Exit", style = MaterialTheme.typography.labelLarge)
                        }
                        Button(onClick = {

                            navController.popBackStack()
                        }) {
                            Text(text = "Add", style = MaterialTheme.typography.labelLarge)
                        }


                    }


                }
            }
        }
    )


}
