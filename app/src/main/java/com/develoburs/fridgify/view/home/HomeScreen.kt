package com.develoburs.fridgify.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.develoburs.fridgify.viewmodel.RecipeListViewModel

@Composable
fun HomeScreen(){
    val viewModel: RecipeListViewModel = viewModel(factory = viewModelFactory {
        initializer {
            RecipeListViewModel()
        }
    })

    val allRecipes = viewModel.recipe.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            itemsIndexed(allRecipes.value) { index, recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = {},
                )
            }
        }
    }
}