package com.develoburs.fridgify.model.api
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.createRecipe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import com.develoburs.fridgify.model.RecipeLikeResponse
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipe-api")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int
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
    ): List<RecipeLikeResponse>

    @POST("recipe-api/like/{recipeId}")
    suspend fun likeRecipe(
        @Path("recipeId") recipeId: String,
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    )
}
