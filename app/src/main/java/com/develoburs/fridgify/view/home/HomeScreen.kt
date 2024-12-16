package com.develoburs.fridgify.view.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.ui.theme.BlackColor
import com.develoburs.fridgify.ui.theme.BlueColor
import com.develoburs.fridgify.ui.theme.BurgundyColor
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.ui.theme.CreamColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.LightCharcoalColor
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.ui.theme.OrangeColor
import com.develoburs.fridgify.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: RecipeListViewModel = viewModel()) {
    val allRecipes = viewModel.recipe.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    if (allRecipes.value.isEmpty()) {
        with(viewModel) { getRecipesList() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.home),
                        color = CharcoalColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_icon),
                            contentDescription = "Filter",
                            tint = CharcoalColor
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
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CreamColor2)
                )

                val recipesToShow = allRecipes.value

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 21.dp)
                ) {
                    itemsIndexed(recipesToShow) { index, recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                navController.navigate("recipeDetails/${recipe.id}")
                            },
                            onEditClick = {
                                navController.navigate("editRecipe/${recipe.id}")
                            },
                            isProfileScreen = false
                        )

                        if (index == viewModel.recipe.collectAsState().value.lastIndex) {
                            viewModel.getRecipesList()
                        }
                    }

                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
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

                        }
                    }

                }

                if (showFilterSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showFilterSheet = false },
                        containerColor = CharcoalColor
                    ) {
                        FilterSheetContent(
                            onDismiss = { showFilterSheet = false }
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun FilterSheetContent(
    onDismiss: () -> Unit
) {
    var cookingTimeMin by remember { mutableStateOf(0f) }
    var cookingTimeMax by remember { mutableStateOf(120f) }

    var calorieMin by remember { mutableStateOf(0f) }
    var calorieMax by remember { mutableStateOf(1000f) }

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
    val categories = categoryMap.values.toList()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(text = "Filter", style = MaterialTheme.typography.labelMedium, color = CreamColor)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cooking Time",
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic
            ), color = CreamColor
        )
        Row {
            Text(
                "Min: ${cookingTimeMin.toInt()} mins",
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall, color = CreamColor
            )
            Text(
                "Max: ${cookingTimeMax.toInt()} mins",
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End, color = CreamColor
            )
        }
        RangeSlider(
            value = cookingTimeMin..cookingTimeMax,
            onValueChange = { range ->
                cookingTimeMin = range.start
                cookingTimeMax = range.endInclusive
            },
            valueRange = 0f..120f,
            steps = 0,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = OrangeColor,
                activeTrackColor = OrangeColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Calories",
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic
            ), color = CreamColor
        )
        Row {
            Text(
                "Min: ${calorieMin.toInt()} kcal",
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall, color = CreamColor
            )
            Text(
                "Max: ${calorieMax.toInt()} kcal",
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End, color = CreamColor
            )
        }
        RangeSlider(
            value = calorieMin..calorieMax,
            onValueChange = { range ->
                calorieMin = range.start
                calorieMax = range.endInclusive
            },
            valueRange = 0f..1000f,
            steps = 0,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = OrangeColor,
                activeTrackColor = OrangeColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Categories",
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 20.sp,
                fontStyle = FontStyle.Italic
            ), color = CreamColor
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = { isDropdownExpanded = true },
                modifier = Modifier
                    .align(Alignment.CenterStart),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = selectedCategory ?: "Select a category",
                    style = MaterialTheme.typography.bodySmall,
                    color = CreamColor,
                )
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.background(color = LightCharcoalColor)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodySmall,
                                color = CreamColor
                            )
                        },
                        onClick = {
                            selectedCategory = category
                            isDropdownExpanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = OrangeColor,
                        ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.labelMedium,
                    color = CreamColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    //todo apply filter functionality
                    Log.d("test", "Selected Filters:")
                    Log.d(
                        "test",
                        "Cooking Time: Min = ${cookingTimeMin.toInt()}, Max = ${cookingTimeMax.toInt()}"
                    )
                    Log.d(
                        "test",
                        "Calorie: Min = ${calorieMin.toInt()}, Max = ${calorieMax.toInt()}"
                    )
                    Log.d("test", "Category: ${selectedCategory ?: "None"}")
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeColor
                )
            ) {
                Text(
                    text = "Apply filters",
                    style = MaterialTheme.typography.labelMedium,
                    color = CreamColor
                )
            }
        }
    }
}
