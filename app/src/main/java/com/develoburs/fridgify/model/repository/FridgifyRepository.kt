package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Comment
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

interface FridgifyRepository {

    suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getRecipeList(
        limit: Int,
        pageNumber: Int,
        cookingTimeMin: Int?,
        cookingTimeMax: Int?,
        calorieMin: Int?,
        calorieMax: Int?,
        category: String?
    ): List<Recipe>

    suspend fun getPersonalizedRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getPersonalizedRecipeList(
        limit: Int,
        pageNumber: Int,
        cookingTimeMin: Int?,
        cookingTimeMax: Int?,
        calorieMin: Int?,
        calorieMax: Int?,
        category: String?
    ): List<Recipe>

    suspend fun getUserRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getRecipeById(id: String): Recipe

    suspend fun getRecipeIngredients(id: String): List<Food>

    suspend fun likeRecipe(recipeId: String, userId: String)

    suspend fun getUserLikedRecipes(userId: String): List<String>

    suspend fun getFoodList(category: String? = null): List<Food>

    suspend fun getNotInFridge(category: String? = null): List<Food>

    suspend fun addFood(ingredientIds: List<Int>)

    suspend fun removeFood(ingredientIds: List<Int>)

    suspend fun saveRecipe(recipeId: String, userId: String)

    suspend fun getUserSavedRecipes(userId: String): List<String>

    suspend fun getSaveCount(recipeId: String): Int

    suspend fun fetchComments(recipeId: String): List<Comment>

    suspend fun addComment(recipeId: String, userId: String, commentText: String)

    suspend fun deleteComment(recipeId: String, commentId: String, userId: String)

}
