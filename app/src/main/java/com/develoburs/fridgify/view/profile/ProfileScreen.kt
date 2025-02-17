package com.develoburs.fridgify.view.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.develoburs.fridgify.R
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.ui.theme.CharcoalColor
import com.develoburs.fridgify.ui.theme.CreamColor2
import com.develoburs.fridgify.ui.theme.DarkOrangeColor
import com.develoburs.fridgify.ui.theme.LightOrangeColor
import com.develoburs.fridgify.view.home.RecipeCard
import com.develoburs.fridgify.viewmodel.RecipeListViewModel

import androidx.compose.ui.res.painterResource
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = viewModel(),
    repository: FridgifyRepositoryImpl
) {
    val allRecipes = viewModel.userrecipe.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val userRecipeCount by viewModel.userRecipeCount.collectAsState(initial = null)
    val userLikeCount by viewModel.userLikeCount.collectAsState(initial = null)

    // Initial data loading
    LaunchedEffect(Unit) {
        if (userRecipeCount == null) {
            viewModel.getUserRecipeCount()
        }
        if (userLikeCount == null) {
            viewModel.getUserLikeCount()
        }
        if (allRecipes.value.isEmpty()) {
            viewModel.getUserRecipesList()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile),
                        color = CharcoalColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.height(50.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addRecipe") },
                shape = CircleShape,
                containerColor = DarkOrangeColor,
                contentColor = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Recipe")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamColor2)
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 21.dp)
            ) {
                // Profile Header Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 10.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(LightOrangeColor, LightOrangeColor)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${repository.getUserFirstName()} ${repository.getUserLastName()}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.menu_book),
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Text(
                                        text = userRecipeCount?.toString() ?: "-",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }

                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.favorite_icon),
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Text(
                                        text = userLikeCount?.toString() ?: "-",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.clearAllRecipes()
                                    viewModel.usr_currentPage = 0
                                    viewModel.getUserRecipesList()
                                    viewModel.getUserRecipeCount()
                                    viewModel.getUserLikeCount()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { navController.navigate("SettingsScreen") }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (allRecipes.value.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No recipes found.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = CharcoalColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Do you want to add a recipe?",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkOrangeColor,
                                modifier = Modifier.clickable {
                                    navController.navigate("addRecipe")
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Refresh The Page",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkOrangeColor,
                                modifier = Modifier.clickable {
                                    viewModel.clearAllRecipes()
                                    viewModel.usr_currentPage = 0
                                    viewModel.getUserRecipesList()
                                    viewModel.getUserRecipeCount()
                                    viewModel.getUserLikeCount()
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(allRecipes.value) { index, recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = {
                                    navController.navigate("recipeDetails/${recipe.id}")
                                },
                                onEditClick = {
                                    navController.navigate("editRecipe/${recipe.id}")
                                },
                                isProfileScreen = true
                            )
                            if (index == allRecipes.value.lastIndex) {
                                viewModel.getUserRecipesList()
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
                }
            }
        }
    }
}