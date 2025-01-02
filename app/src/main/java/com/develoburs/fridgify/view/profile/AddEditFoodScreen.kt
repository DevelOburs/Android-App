package com.develoburs.fridgify.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.view.fridge.FoodCard
import com.develoburs.fridgify.viewmodel.FridgeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFoodScreen(navController: NavController, viewModel: FridgeViewModel = viewModel() ,onBack: () -> Unit) {

    val allFoods by viewModel.allfood.collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }


    if (allFoods.isEmpty()) {
        viewModel.getAllFoodList() // Call the method to fetch recipes
    }
    LaunchedEffect(Unit) {
        viewModel.getAllFoodList()
    }
    val filteredFoods = remember(searchQuery, allFoods) {
        allFoods.filter { it.Name.contains(searchQuery, ignoreCase = true) }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var quantity by remember { mutableStateOf("") }
    val selectedItems = viewModel.selectedFoods

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
                        placeholder = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) },
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
                                            color = if (selectedItems.map { it.id }.contains(food.id)) Color.Green else Color.Transparent,
                                            shape = RectangleShape
                                        )
                                        .clickable {
                                            if (selectedItems.map { it.id }.contains(food.id)) {
                                                selectedItems.removeIf { it.id == food.id }
                                            } else {
                                                selectedFood = food
                                                showDialog = true
                                            }
                                        }
                                ) {
                                    FoodCard(
                                        food = food,
                                        onClick = {
                                            if (selectedItems.map { it.id }.contains(food.id)) {
                                                selectedItems.removeIf { it.id == food.id }
                                            } else {
                                                selectedFood = food
                                                showDialog = true
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
                        Button(colors = ButtonDefaults.buttonColors(
                            containerColor = com.develoburs.fridgify.ui.theme.BurgundyColor,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.White
                        ),modifier = Modifier.weight(1f),onClick = { navController.popBackStack() }) {
                            Text(text = "Exit", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(colors = ButtonDefaults.buttonColors(
                            containerColor = com.develoburs.fridgify.ui.theme.BrightGreenColor,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.White
                        ),modifier = Modifier.weight(1f),onClick = {
                            navController.popBackStack()
                        }) {
                            Text(text = "Add", style = MaterialTheme.typography.labelMedium)
                        }


                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Enter Quantity") },
                            text = {
                                Column {
                                    Text("How many ${selectedFood?.Name} would you like?")
                                    TextField(
                                        value = "", //
                                        onValueChange = { quantity = it },
                                        label = { Text("Quantity") },
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.develoburs.fridgify.ui.theme.BrightGreenColor,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.White,
                                        disabledContentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp), // Adjust padding to make it smaller
                                    onClick = {
                                        selectedFood?.let {
                                            it.Quantity = quantity
                                            selectedItems.add(it)
                                        }
                                        showDialog = false
                                    }
                                ) {
                                    Text("OK", style = MaterialTheme.typography.labelSmall)
                                }
                            },
                            dismissButton = {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.develoburs.fridgify.ui.theme.BurgundyColor,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.White,
                                        disabledContentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp), // Adjust padding to make it smaller
                                    onClick = { showDialog = false }
                                ) {
                                    Text("Cancel", style = MaterialTheme.typography.labelSmall)
                                }
                            }

                        )
                    }

                }
            }
        }
    )


}
