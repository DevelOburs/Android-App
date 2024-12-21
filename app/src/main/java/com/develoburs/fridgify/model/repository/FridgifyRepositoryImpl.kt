package com.develoburs.fridgify.model.repository

import android.util.Log
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.api.RetrofitInstance.api
import com.develoburs.fridgify.model.api.RetrofitInstance.fridgeapi

import android.content.Context
import com.develoburs.fridgify.model.api.FridgeApi
import com.develoburs.fridgify.model.createRecipe

class FridgifyRepositoryImpl : FridgifyRepository {

    private var token: String = ""
    private var userName: String = ""
    private var firstName: String = ""
    private var userId: Int = 0
    private var lastName: String = ""
    private var email: String = ""

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String {
        return token
    }
    fun setUserID(UserID: Int) {
        userId = UserID
    }

    fun getUserID(): Int {
        return userId
    }

    fun setUserName(UserName: String) {
        userName = UserName
    }

    fun getUserName(): String {
        return userName
    }

    fun setUserFirstName(FirstName: String) {
        firstName = FirstName
    }

    fun getUserFirstName(): String {
        return firstName
    }

    fun setUserLastName(LastName: String) {
        lastName = LastName
    }

    fun getUserLastName(): String {
        return lastName
    }

    fun setUserEmail(Email:String) {
        email = Email
    }

    fun getUserEmail(): String {
        return email
    }

    suspend fun getUserLikeCount(): Int {
        try {
            // Use getToken function to retrieve the token
            val count = api.getUserTotalLike(
                userId = getUserID(),
                token = "Bearer ${getToken()}"
            )
//            Log.d("Recipe List", recipes.toString())
            return count
        } catch (e: Exception) {
            throw Exception("Failed", e)
        }
    }

    suspend fun getUserRecipeCount(): Int {
        try {
            // Use getToken function to retrieve the token
            val count = api.getUserRecipeCount(
                userId = getUserID(),
                token = "Bearer ${getToken()}"
            )
//            Log.d("Recipe List", recipes.toString())
            return count
        } catch (e: Exception) {
            throw Exception("Failed", e)
        }
    }

    override suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getRecipes(
                token = "Bearer ${getToken()}",
                limit = limit,
                pageNumber = pageNumber
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getUserRecipeList(limit: Int, pageNumber: Int): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getRecipesByUserId(
                userId = getUserID(),
                token = "Bearer ${getToken()}",
                limit = limit,
                pageNumber = pageNumber
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    suspend fun getUserLikedList(): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getUserLikedRecipes(
                userId = getUserID(),
                token = "Bearer ${getToken()}"
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get liked recipes, Bearer ${getToken()}", e)
        }
    }

    suspend fun getUserSavedList(): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getUserSavedRecipes(
                userId = getUserID(),
                token = "Bearer ${getToken()}"
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get saved recipes, Bearer ${getToken()}", e)
        }
    }



    override suspend fun getRecipeById(id: String): Recipe {
        return try {
            val token = "Bearer ${getToken()}" // Dynamically fetch the token
            val recipe = api.getRecipeById(id, token) // Pass the token in the API call
            Log.d("Recipe", "Fetched recipe: $recipe")
            recipe
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to get recipe detail for ID: $id", e)
            throw Exception("Failed to get recipe detail")
        }
    }


    /*
        private val mockFoods = listOf(
            Food(
                id = 1, Name = "Radish", ImageUrl = "Radish", Category =
            ),
            Food(id = 2, Name = "Blueberry", ImageUrl = "Blueberry"),
            Food(id = 3, Name = "Guava", ImageUrl = "Guava"),
            Food(id = 4, Name = "Mushroom", ImageUrl = "Mushroom"),
            Food(id = 5, Name = "Kiwi", ImageUrl = "Kiwi"),
            Food(id = 6, Name = "Raspberry", ImageUrl = "Raspberry"),
            Food(id = 7, Name = "Avocado", ImageUrl = "Avocado"),
            Food(id = 8, Name = "Papaya", ImageUrl = "Papaya"),
            Food(id = 9, Name = "Zucchini", ImageUrl = "Zucchini"),

        )*/

    suspend fun addRecipe(recipe: createRecipe) {
        try {
            // Use getToken function to retrieve the token
            api.addRecipe(recipe = recipe, token = "Bearer ${getToken()}")
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    suspend fun updateRecipe(id: Int, recipe: createRecipe) {
        try {
            // Use getToken function to retrieve the token
            api.updateRecipe(id = id, recipe = recipe, token = "Bearer ${getToken()}")
        } catch (e: Exception) {
            throw Exception("Failed to update recipes, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getRecipeIngredients(id: String): List<Food> {
        try {
            // Use getToken function to retrieve the token
            val foods = api.getRecipeIngredients(id, token)
            Log.d("Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            throw Exception("Failed to get foods, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getFoodList(category: String?): List<Food> {
        try {
            Log.d("API_CALL", "getFoodList - Formatted Category: $category")

            val foods = fridgeapi.getFood(
                token = "Bearer ${getToken()}",
                userId = getUserID(),
                category = if (category == null) null else listOf(category)

            )
            Log.d("API_CALL", "getFoodList - Formatted Category: $category")


            return foods
        } catch (e: Exception) {

            throw Exception("Failed to fetch food list", e)
        }
    }

    override suspend fun getNotInFridge(category: String?): List<Food> {
        try {
            val foods = fridgeapi.getNotInFridge(
                "Bearer ${getToken()}",
                userId = getUserID(),
                category = if (category == null) null else listOf(category)
            )
            Log.d("Not In Fridge Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to get not in fridge foods", e)
            throw Exception("Failed to get not in fridge foods", e)
        }
    }

    override suspend fun addFood(ingredientIds: List<Int>) {
        try {
            val request = FridgeApi.AddFoodRequest(ingredientIds)
            fridgeapi.addFood(
                token = "Bearer ${getToken()}",
                userId = getUserID(),
                ingredientIds = request
            )
            Log.d("FridgifyRepositoryImpl", "Food added successfully")
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to add food", e)
            throw Exception("Failed to add food: ${e.message}", e)
        }
    }


    override suspend fun removeFood(ingredientIds:  List<Int>) {
        try {
            val request = FridgeApi.DeleteFoodRequest(ingredientIds)
            fridgeapi.removeFood(
                "Bearer ${getToken()}",
                userId = getUserID(),
                ingredientIds = request
            )
            Log.d("FridgifyRepositoryImpl", "Food removed successfully")
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to remove food", e)
            throw Exception("Failed to remove food", e)
        }
    }






}