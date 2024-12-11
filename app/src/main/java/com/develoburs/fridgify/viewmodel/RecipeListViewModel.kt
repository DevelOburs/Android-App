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
    private val _userrecipe = MutableStateFlow<List<Recipe>>(emptyList())
    val userrecipe: StateFlow<List<Recipe>> = _userrecipe
    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail

    private var currentPage = 0
    private val pageSize = 10
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private var isLastPage = false

    private val tag = "RecipeListViewModel"

    init {
        getRecipesList()
        getUserRecipesList()
    }

    fun getRecipesList() {
        if (_isLoading.value || isLastPage) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(tag, "Getting recipes for page:$currentPage and limit:$pageSize")
                val recipeList = repository.getRecipeList(pageSize, currentPage)

                if (recipeList.isEmpty()) {
                    isLastPage = true
                } else {
                    _recipe.value = _recipe.value + recipeList
                    currentPage++
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserRecipesList() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeList = repository.getUserRecipeList()
                _userrecipe.value = _userrecipe.value + recipeList
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
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
