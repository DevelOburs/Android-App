package com.develoburs.fridgify.model.api
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipe-api")
    suspend fun getRecipes(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int,
        @Query("pageNumber") pageNumber: Int
    ): List<Recipe>

    @GET("recipe-api/getRecipes/{userId}")
    suspend fun getRecipesByUserId(
        @Path("userId") userId: Int,
        @Header("Authorization") token: String
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
}
