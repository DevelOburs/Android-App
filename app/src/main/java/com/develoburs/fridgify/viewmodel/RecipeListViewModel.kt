package com.develoburs.fridgify.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.develoburs.fridgify.model.Comment
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.createRecipe
import kotlinx.coroutines.withContext

class RecipeListViewModel(
    private val navController: NavController,
    private val repository: FridgifyRepositoryImpl
) : ViewModel() {
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

    private val _recipeLikeCount = MutableStateFlow<Int?>(null)
    val recipeLikeCount: StateFlow<Int?> = _recipeLikeCount

    private val _userLikedRecipes = MutableStateFlow<List<String>>(emptyList())
    val userLikedRecipes: StateFlow<List<String>> = _userLikedRecipes

    // State for user-saved recipes
    private val _userSavedRecipes = MutableStateFlow<List<String>>(emptyList())
    val userSavedRecipes: StateFlow<List<String>> = _userSavedRecipes

    // State for the save count of a specific recipe
    private val _saveCount = MutableStateFlow(0)
    val saveCount: StateFlow<Int> = _saveCount

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _isLiking = MutableStateFlow(false)
    val isLiking: StateFlow<Boolean> = _isLiking

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    // Fetch comments for a specific recipe
    private val _isCommentsLoading = MutableStateFlow(false)
    val isCommentsLoading: StateFlow<Boolean> = _isCommentsLoading

    private val _isAddingComment = MutableStateFlow(false)
    val isAddingComment: StateFlow<Boolean> = _isAddingComment

    private val _isDeletingComment = MutableStateFlow(false)
    val isDeletingComment: StateFlow<Boolean> = _isDeletingComment

    var name = mutableStateOf("")
    var description = mutableStateOf("")
    var calories = mutableStateOf(0)
    var cookingTime = mutableStateOf(0)
    var category = mutableStateOf("APPETIZERS_AND_SNACKS")

    fun setIsLikingLoading(isLoading: Boolean) {
        _isLiking.value = isLoading
    }

    fun setIsSavingLoading(isLoading: Boolean) {
        _isSaving.value = isLoading
    }

    private var currentPage = 0
    private val pageSize = 10

    var usr_currentPage = 0
    private val usr_pageSize = 10
    private var usr_isLastPage = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private var isLastPage = false

    private val _showNoRecipeDialog = MutableStateFlow(false)
    val showNoRecipeDialog: StateFlow<Boolean> = _showNoRecipeDialog

    private val _userRecipeCount = MutableStateFlow<Int?>(null)
    val userRecipeCount: StateFlow<Int?> get() = _userRecipeCount

    private val _userLikeCount = MutableStateFlow<Int?>(null)
    val userLikeCount: StateFlow<Int?> get() = _userLikeCount

    private val _commentCount = MutableStateFlow(0)
    val commentCount: StateFlow<Int> = _commentCount

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> get() = _imageUrl

    var hasLoaded: StateFlow<Boolean> = MutableStateFlow(false)

    private val tag = "RecipeListViewModel"

    enum class RecipeFetchType {
        ALL_RECIPES,
        FILTERED_RECIPES,
        PERSONALIZED_RECIPES,
        FILTERED_PERSONALIZED_RECIPES
    }

    var currentFetchType: RecipeFetchType = RecipeFetchType.ALL_RECIPES
        private set

    private var lastFilterParams: FilterParams? = null

    data class FilterParams(
        val cookingTimeMin: Int? = null,
        val cookingTimeMax: Int? = null,
        val calorieMin: Int? = null,
        val calorieMax: Int? = null,
        val category: String? = null,
        val searchQuery: String? = null
    )

    fun resetPageCount(){
        Log.d("test", "reset page count")
        currentPage = 0
        isLastPage = false
        _recipe.value = emptyList()
    }

    fun clearAllRecipes() {
        _userrecipe.value = emptyList() // Set the value to an empty list
    }

    fun fetchNextPage() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                when (currentFetchType) {
                    RecipeFetchType.ALL_RECIPES -> {
                        getRecipesList()
                    }
                    RecipeFetchType.FILTERED_RECIPES -> {
                        getRecipesList(
                            lastFilterParams?.cookingTimeMin,
                            lastFilterParams?.cookingTimeMax,
                            lastFilterParams?.calorieMin,
                            lastFilterParams?.calorieMax,
                            lastFilterParams?.category,
                            lastFilterParams?.searchQuery
                        )
                    }
                    RecipeFetchType.PERSONALIZED_RECIPES -> {
                        getPersonalizedRecipes()
                    }
                    RecipeFetchType.FILTERED_PERSONALIZED_RECIPES -> {
                        getPersonalizedRecipes(
                            lastFilterParams?.cookingTimeMin,
                            lastFilterParams?.cookingTimeMax,
                            lastFilterParams?.calorieMin,
                            lastFilterParams?.calorieMax,
                            lastFilterParams?.category,
                            lastFilterParams?.searchQuery
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Error fetching recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipesList() {
        currentFetchType = RecipeFetchType.ALL_RECIPES
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                Log.d(tag, "ALL_RECIPES for page:$currentPage and limit:$pageSize")
                val recipeList = repository.getRecipeList(pageSize, currentPage)
                if(currentPage == 0 && recipeList.isEmpty()){
                    setShowNoRecipeDialog(true)
                }
                if (recipeList.isEmpty()) {
                    isLastPage = true
                } else {
                    _recipe.value = _recipe.value + recipeList
                    currentPage++
                    if(recipeList.size < 10) isLastPage = true
                }
                Log.d(tag, "${recipeList.lastOrNull()?.Name}")
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipesList(
        cookingTimeMin: Int? = null,
        cookingTimeMax: Int? = null,
        calorieMin: Int? = null,
        calorieMax: Int? = null,
        category: String? = null,
        searchQuery: String? = null
    ) {
        currentFetchType = RecipeFetchType.FILTERED_RECIPES
        lastFilterParams = FilterParams(cookingTimeMin, cookingTimeMax, calorieMin, calorieMax, category, searchQuery)
        viewModelScope.launch (Dispatchers.IO) {
            _isLoading.value = true
            try {
                Log.d(tag, "FILTERED_RECIPES for page:$currentPage and limit:$pageSize")
                val recipeList = repository.getRecipeList(
                    pageSize, currentPage, cookingTimeMin, cookingTimeMax, calorieMin, calorieMax, category, searchQuery
                )
                if(currentPage == 0 && recipeList.isEmpty()){
                    setShowNoRecipeDialog(true)
                }
                if (recipeList.isEmpty()) {
                    isLastPage = true
                } else {
                    _recipe.value = _recipe.value + recipeList
                    currentPage++
                    if(recipeList.size < 10) isLastPage = true
                }
                Log.d(tag, "${recipeList.lastOrNull()?.Name}")
            } catch (e: Exception) {
                Log.e(tag, "Error fetching recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPersonalizedRecipes() {
        currentFetchType = RecipeFetchType.PERSONALIZED_RECIPES
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                Log.d(tag, "PERSONALIZED_RECIPES for page:$currentPage and limit:$pageSize")
                val recipeList = repository.getPersonalizedRecipeList(pageSize, currentPage)
                if(currentPage == 0 && recipeList.isEmpty()){
                    setShowNoRecipeDialog(true)
                }
                if (recipeList.isEmpty()) {
                    isLastPage = true
                } else {
                    _recipe.value = _recipe.value + recipeList
                    currentPage++
                    if(recipeList.size < 10) isLastPage = true
                }
                Log.d(tag, "${recipeList.lastOrNull()?.Name}")
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPersonalizedRecipes(
        cookingTimeMin: Int? = null,
        cookingTimeMax: Int? = null,
        calorieMin: Int? = null,
        calorieMax: Int? = null,
        category: String? = null,
        searchQuery: String? = null
    ) {
        currentFetchType = RecipeFetchType.FILTERED_PERSONALIZED_RECIPES
        lastFilterParams = FilterParams(cookingTimeMin, cookingTimeMax, calorieMin, calorieMax, category, searchQuery)
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                Log.d(tag, "FILTERED_PERSONALIZED_RECIPES for page:$currentPage and limit:$pageSize")
                val recipeList = repository.getPersonalizedRecipeList(
                    pageSize, currentPage, cookingTimeMin, cookingTimeMax, calorieMin, calorieMax, category, searchQuery
                )
                if(currentPage == 0 && recipeList.isEmpty()){
                    setShowNoRecipeDialog(true)
                }
                if (recipeList.isEmpty()) {
                    isLastPage = true
                } else {
                    _recipe.value = _recipe.value + recipeList
                    currentPage++
                    if(recipeList.size < 10) isLastPage = true
                }
                Log.d(tag, "${recipeList.lastOrNull()?.Name}")
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch personalized recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setShowNoRecipeDialog(newValue: Boolean){
        _showNoRecipeDialog.value = newValue
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
                Log.d(tag, "Getting recipes for user page:$usr_currentPage and limit:$usr_pageSize")
                val recipeList = repository.getUserRecipeList(usr_pageSize, usr_currentPage)
                if(currentPage == 0 && recipeList.isEmpty()){
                    setShowNoRecipeDialog(true)
                }
                if (recipeList.isEmpty()) {
                    usr_isLastPage = true
                } else {
                    _userrecipe.value = _userrecipe.value + recipeList
                    usr_currentPage++
                    if(recipeList.size < 10) isLastPage = true
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to fetch recipe list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipeById(id: String) {
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
            _isLiking.emit(true) // Start loading
            val isCurrentlyLiked = _userLikedRecipes.value.contains(recipeId)

            // Optimistically update the like count
            _recipeDetail.value = _recipeDetail.value?.let { recipe ->
                recipe.copy(
                    Likes = if (isCurrentlyLiked) (recipe.Likes ?: 0) - 1 else (recipe.Likes ?: 0) + 1
                )
            }

            // Optimistically update the liked recipes list
            if (isCurrentlyLiked) {
                _userLikedRecipes.emit(_userLikedRecipes.value - recipeId)
            } else {
                _userLikedRecipes.emit(_userLikedRecipes.value + recipeId)
            }

            try {
                repository.likeRecipe(recipeId, userId) // Perform the API call
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to like/unlike recipe", e)

                // Revert the optimistic update on failure
                _recipeDetail.value = _recipeDetail.value?.let { recipe ->
                    recipe.copy(
                        Likes = if (isCurrentlyLiked) (recipe.Likes ?: 0) + 1 else (recipe.Likes ?: 0) - 1
                    )
                }
                if (isCurrentlyLiked) {
                    _userLikedRecipes.emit(_userLikedRecipes.value + recipeId)
                } else {
                    _userLikedRecipes.emit(_userLikedRecipes.value - recipeId)
                }
            } finally {
                _isLiking.emit(false) // Stop loading
            }
        }
    }




    fun fetchUserLikedRecipes(userId: String) {
        viewModelScope.launch {
            try {
                setIsLikingLoading(true) // Set loading state
                val likedRecipes = repository.getUserLikedRecipes(userId)
                _userLikedRecipes.value = likedRecipes // Update the state with fetched data
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Error fetching liked recipes: $e")
            } finally {
                setIsLikingLoading(false) // Turn off loading state
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

    fun deleteRecipe(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add recipe to the repository
                repository.deleteRecipe(id)
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to delete recipe", e)
            }
        }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            try {
                // Assuming you want to directly update the LiveData with the URI
                _imageUrl.value = uri.toString() // Convert URI to string if needed for the LiveData

            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Image upload failed", e)
            }
        }
    }


    fun updateRecipe(id: Int, updatedRecipe: createRecipe) {
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
                Log.d("ViewModel", "Recipe set to state: $food")
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to fetch recipe detail", e)
            }
        }
    }

    // Fetch user saved recipes
    fun fetchUserSavedRecipes(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setIsSavingLoading(true) // Set loading state
                val savedRecipes = repository.getUserSavedRecipes(userId)
                _userSavedRecipes.emit(savedRecipes)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Log error or display an error message
                    println("Failed to fetch user saved recipes: $e")
                }
            } finally {
                setIsSavingLoading(false) // Turn off loading state
            }
        }
    }

    // Save or unsave a recipe
    fun saveOrUnsaveRecipe(recipeId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isSaving.emit(true) // Start loading
            try {
                val isCurrentlySaved = _userSavedRecipes.value.contains(recipeId)
                repository.saveRecipe(recipeId, userId)
                if (isCurrentlySaved) {
                    _userSavedRecipes.emit(_userSavedRecipes.value - recipeId)
                } else {
                    _userSavedRecipes.emit(_userSavedRecipes.value + recipeId)
                }
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to save/unsave recipe", e)
            } finally {
                _isSaving.emit(false) // Stop loading
            }
        }
    }


    // Fetch save count for a specific recipe
    fun fetchSaveCount(recipeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = repository.getSaveCount(recipeId)
                _saveCount.emit(count)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Log error or display an error message
                    println("Failed to fetch save count for recipe: $recipeId, Error: $e")
                }
            }
        }
    }


    fun fetchComments(recipeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isCommentsLoading.value = true // Set loading to true
            try {
                val fetchedComments = repository.fetchComments(recipeId)
                _comments.emit(fetchedComments)
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch comments for recipe: $recipeId", e)
            } finally {
                _isCommentsLoading.value = false // Set loading to false
            }
        }
    }

    fun addComment(recipeId: String, userId: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isAddingComment.value = true // Set loading state to true
            try {
                repository.addComment(recipeId, userId, comment)
                fetchComments(recipeId) // Refresh comments after successfully adding

                // Increment the comment count in the Recipe object
                _recipeDetail.value = _recipeDetail.value?.copy(
                    Comments = (_recipeDetail.value?.Comments ?: 0) + 1
                )
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to add comment", e)
            } finally {
                _isAddingComment.value = false // Reset loading state
            }
        }
    }

    fun deleteComment(commentId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ViewModel", "Starting deletion...")
            _isDeletingComment.value = true
            try {
                repository.deleteComment(commentId, userId)
                _comments.value = _comments.value.filterNot { it.id == commentId }
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Failed to delete comment", e)
            } finally {
                _isDeletingComment.value = false
                Log.d("ViewModel", "Deletion complete.")
            }
        }
    }

}
