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
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.createRecipe

class RecipeListViewModel(private val navController: NavController, private val repository: FridgifyRepositoryImpl) : ViewModel() {
    private val _recipe = MutableStateFlow<List<Recipe>>(emptyList())
    val recipe: StateFlow<List<Recipe>> = _recipe
    private val _userrecipe = MutableStateFlow<List<Recipe>>(emptyList())
    val userrecipe: StateFlow<List<Recipe>> = _userrecipe
    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail

    private val _food = MutableStateFlow<List<Food>>(emptyList())
    val food: StateFlow<List<Food>> = _food

    private val _recipeLikeCount = MutableStateFlow<Int?>(null)
    val recipeLikeCount: StateFlow<Int?> = _recipeLikeCount

    private val _userLikedRecipes = MutableStateFlow<List<String>>(emptyList())
    val userLikedRecipes: StateFlow<List<String>> = _userLikedRecipes

    private var currentPage = 0
    private val pageSize = 10
    private var usr_currentPage = 0
    private val usr_pageSize = 10
    private var usr_isLastPage = false
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
        if (_isLoading.value || usr_isLastPage) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(tag, "Getting recipes for page:$usr_currentPage and limit:$usr_pageSize")
                val recipeList = repository.getUserRecipeList(usr_pageSize, usr_currentPage)

                if (recipeList.isEmpty()) {
                    usr_isLastPage = true
                } else {
                    _userrecipe.value = _userrecipe.value + recipeList
                    usr_currentPage++
                }
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

    fun likeOrUnlikeRecipe(recipeId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isCurrentlyLiked = _userLikedRecipes.value.contains(recipeId)

                // Call the like endpoint (assumed to toggle the like state)
                repository.likeRecipe(recipeId, userId)

                // Optimistically update UI state
                if (isCurrentlyLiked) {
                    // Unlike the recipe
                    _userLikedRecipes.value = _userLikedRecipes.value - recipeId
                    _recipeDetail.value = _recipeDetail.value?.let { recipe ->
                        recipe.copy(Likes = (recipe.Likes ?: 0) - 1) // Decrement like count
                    }
                } else {
                    // Like the recipe
                    _userLikedRecipes.value = _userLikedRecipes.value + recipeId
                    _recipeDetail.value = _recipeDetail.value?.let { recipe ->
                        recipe.copy(Likes = (recipe.Likes ?: 0) + 1) // Increment like count
                    }
                }
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to toggle like for recipe: $recipeId", e)
            }
        }
    }


    fun fetchUserLikedRecipes(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val likedRecipes = repository.getUserLikedRecipes(userId)
                _userLikedRecipes.value = likedRecipes // Ensure the state is updated
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch user liked recipes", e)
            }
        }
    }

    fun addRecipe(id: String, newRecipe: createRecipe) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add recipe to the repository
                repository.addRecipe(id, newRecipe)

                // Update the local state flow with the new recipe list
                //_recipe.value = _recipe.value + newRecipe
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

    fun getRecipeIngredients(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getRecipeIngredients(id)
                _food.value = foodList
                Log.d("ViewModel", "Recipe set to state: $recipe")
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch recipe detail", e)
            }
        }
    }
}
