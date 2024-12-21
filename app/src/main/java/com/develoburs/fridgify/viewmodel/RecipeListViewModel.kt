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
    private val _userlikedrecipe = MutableStateFlow<List<Recipe>>(emptyList())
    val userlikedrecipe: StateFlow<List<Recipe>> = _userlikedrecipe
    private val _usersavedrecipe = MutableStateFlow<List<Recipe>>(emptyList())
    val usersavedrecipe: StateFlow<List<Recipe>> = _usersavedrecipe
    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail

    private val _food = MutableStateFlow<List<Food>>(emptyList())
    val food: StateFlow<List<Food>> = _food

    private var currentPage = 0
    private val pageSize = 10
    private var usr_currentPage = 0
    private val usr_pageSize = 10
    private var usr_isLastPage = false
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private var isLastPage = false

    private val _userRecipeCount = MutableStateFlow<Int?>(null)
    val userRecipeCount: StateFlow<Int?> get() = _userRecipeCount

    private val _userLikeCount = MutableStateFlow<Int?>(null)
    val userLikeCount: StateFlow<Int?> get() = _userLikeCount

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

    fun getUserLikedRecipesList() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeList = repository.getUserLikedList()
                _userlikedrecipe.value = recipeList
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserLikeCount() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userLikeCount = repository.getUserLikeCount()
                _userLikeCount.value = userLikeCount
                Log.d("Count", " Like Count $userLikeCount")
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserRecipeCount() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRecipeCount = repository.getUserRecipeCount()
                _userRecipeCount.value = userRecipeCount
                Log.d("Count", " Recipe Count $userRecipeCount")
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun getUserSavedRecipesList() {
        if (_isLoading.value) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipeList = repository.getUserSavedList()
                _usersavedrecipe.value = recipeList
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
    fun addRecipe(id: String, newRecipe: createRecipe) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add recipe to the repository
                repository.addRecipe(newRecipe)
                getUserRecipesList()
                // Update the local state flow with the new recipe list
                //_recipe.value = _recipe.value + newRecipe
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to add recipe", e)
            }
        }
    }
    fun updateRecipe(id : Int ,updatedRecipe: createRecipe) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add recipe to the repository
                repository.updateRecipe(id, updatedRecipe)
                getUserRecipesList()
                // Update the local state flow with the new recipe list
                //_recipe.value = _recipe.value + newRecipe
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to add recipe", e)
            }
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
