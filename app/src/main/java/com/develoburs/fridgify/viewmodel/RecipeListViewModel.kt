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

class RecipeListViewModel(private val navController: NavController) : ViewModel() {
    private val repository = FridgifyRepositoryImpl()
    private val _recipe = MutableStateFlow<List<Recipe>>(emptyList())
    val recipe: StateFlow<List<Recipe>> = _recipe

    init {
        getRecipesList()
    }

    private fun getRecipesList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeList = repository.getRecipeList()
                _recipe.value = recipeList
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch recipe list", e)
            }
        }
    }

    fun getRecipeById(recipeId: String?): Recipe? {
        return _recipe.value.find { it.id == recipeId }
    }
}
