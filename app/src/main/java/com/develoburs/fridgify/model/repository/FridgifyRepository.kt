package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

interface FridgifyRepository {

    suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getUserRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getRecipeById(id: String): Recipe

    suspend fun getRecipeIngredients(id: String): List<Food>

    suspend fun getFoodList(category: String? = null): List<Food>
    suspend fun getNotInFridge(category: String? = null): List<Food>

    suspend fun addFood(ingredientIds: List<Int>)
    suspend fun removeFood(ingredientIds: List<Int>)


}
