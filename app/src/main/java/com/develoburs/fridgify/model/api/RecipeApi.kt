package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.Comment
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.createRecipe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipe-api")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int
    ): List<Recipe>

    @GET("recipe-api")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("minCookingTime") cookingTimeMin: Int?,
        @Query("maxCookingTime") cookingTimeMax: Int?,
        @Query("minCalories") calorieMin: Int?,
        @Query("maxCalories") calorieMax: Int?,
        @Query("category") category: String?,
        @Query("search") search: String?
    ): List<Recipe>

    @GET("recipe-api/personalized/{userId}")
    suspend fun getPersonalizedRecipes(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int
    ): List<Recipe>

    @GET("recipe-api/personalized/{userId}")
    suspend fun getPersonalizedRecipes(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("minCookingTime") cookingTimeMin: Int?,
        @Query("maxCookingTime") cookingTimeMax: Int?,
        @Query("minCalories") calorieMin: Int?,
        @Query("maxCalories") calorieMax: Int?,
        @Query("category") category: String?,
        @Query("search") search: String?
    ): List<Recipe>

    @GET("recipe-api/like/userLikedRecipes")
    suspend fun getUserLikedRecipes(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String,
    ): List<Recipe>

    @GET("recipe-api/userRecipeCount")
    suspend fun getUserRecipeCount(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String,
    ): Int

    @GET("recipe-api/like/totalCountOfUser")
    suspend fun getUserTotalLike(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String,
    ): Int

    @GET("recipe-api/save/list/{userId}")
    suspend fun getUserSavedRecipes(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String,
    ): List<Recipe>

    @GET("recipe-api/getRecipes/{userId}")
    suspend fun getRecipesByUserId(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int
    ): List<Recipe>

    @GET("recipe-api/{id}")
    suspend fun getRecipeById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Recipe

    @DELETE("recipe-api/{id}")
    suspend fun deleteRecipeById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Recipe

    @GET("recipe-api/ingredient/{id}")
    suspend fun getRecipeIngredients(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): List<Food>

    @POST("recipe-api") // Define the PUT endpoint
    suspend fun addRecipe(
        @Body recipe: createRecipe, // The updated recipe object
        @Header("Authorization") token: String // Optional authorization token
    )

    @PUT("recipe-api/{id}") // Define the PUT endpoint
    suspend fun updateRecipe(
        @Path("id") id: Int,
        @Body recipe: createRecipe, // The updated recipe object
        @Header("Authorization") token: String // Optional authorization token
    )

    @GET("recipe-api/like/userLikedRecipes")
    suspend fun getUserLikedRecipes(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): List<Recipe>

    @POST("recipe-api/like/{recipeId}")
    suspend fun likeRecipe(
        @Path("recipeId") recipeId: String,
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    )

    @GET("recipe-api/save/list/{userId}")
    suspend fun getUserSavedRecipes(
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    ): List<Recipe>

    @GET("recipe-api/save/{recipeId}/{userId}")
    suspend fun saveRecipe(
        @Path("recipeId") recipeId: String,
        @Path("userId") userId: String,
        @Header("Authorization") token: String
    )

    @GET("recipe-api/save/{recipeId}/count")
    suspend fun getSaveCount(
        @Path("recipeId") recipeId: String
    ): Int

    @GET("recipe-api/comment/{recipeId}")
    suspend fun getComments(
        @Path("recipeId") recipeId: String,
        @Header("Authorization") token: String
    ): List<Comment>

    @POST("recipe-api/comment/{recipeId}")
    suspend fun addComment(
        @Path("recipeId") recipeId: String,
        @Query("userId") userId: String,
        @Body comment: Comment,
        @Header("Authorization") token: String
    )

    @DELETE("recipe-api/comment/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: String,
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<Unit>


}
