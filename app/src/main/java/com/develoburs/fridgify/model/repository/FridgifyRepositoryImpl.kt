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
    private var userId: Int = 0
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

    suspend fun addRecipe(id: String, recipe: createRecipe) {
        try {
            // Use getToken function to retrieve the token
            api.addRecipe(recipe = recipe, token = "Bearer ${getToken()}")
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
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
            val foods = fridgeapi.getFood(
                token = "Bearer ${getToken()}",
                userId = getUserID(),
                category = category?.let { listOf(it) } // Pass category as a list if not null
            )
            Log.d("Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            throw Exception("Failed to fetch food list", e)
        }
    }

    override suspend fun getNotInFridge(nameFilter: String?, categoryFilters: List<String>?): List<Food> {
        try {
            val foods = fridgeapi.getNotInFridge(
                "Bearer ${getToken()}",
                userId = getUserID(),
                nameFilter = nameFilter,
                categoryFilters = categoryFilters
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