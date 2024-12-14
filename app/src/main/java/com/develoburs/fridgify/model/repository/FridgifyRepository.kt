package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

interface FridgifyRepository {

    suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getUserRecipeList(): List<Recipe>

    suspend fun getRecipeById(id: String): Recipe

    suspend fun getRecipeIngredients(id: String): List<Food>

    suspend fun getFoodList(): List<Food>
    suspend fun getNotInFridge(
        nameFilter: String? = null,
        categoryFilters: List<String>? = null
    ): List<Food>

    suspend fun addFood(ingredientIds: List<Int>)
    suspend fun removeFood(ingredientIds:List<Int>)

    suspend fun getCategories(): List<String>

    suspend fun getFoodByCategory(ingredientCategory: String): List<Food>


}
