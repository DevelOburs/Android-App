package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.navigation.NavController

class RecipeListViewModel(private val navController: NavController, private val repository: FridgifyRepositoryImpl) : ViewModel() {
    private val _recipe = MutableStateFlow<List<Recipe>>(emptyList())
    val recipe: StateFlow<List<Recipe>> = _recipe
    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail
    init {
        getRecipesList()
    }

    fun getRecipesList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeList = repository.getRecipeList()
                _recipe.value = recipeList
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch recipe list", e)
            }
        }
    }

    fun getRecipeById(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = repository.getRecipeById(id)
                _recipeDetail.value = recipe
                Log.d("ViewModel", "Recipe set to state: $recipe")
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch recipe detail", e)
            }
        }

    }
    fun addRecipe(newRecipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add recipe to the repository
                repository.addRecipe(newRecipe)

                // Update the local state flow with the new recipe list
                _recipe.value = _recipe.value + newRecipe
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to add recipe", e)
            }
        }
    }
    fun updateRecipe(updatedRecipe: Recipe) {
        // Update the recipe in your list and/or backend
        _recipe.value = _recipe.value.map {
            if (it.id == updatedRecipe.id) updatedRecipe else it
        }
    }
}
