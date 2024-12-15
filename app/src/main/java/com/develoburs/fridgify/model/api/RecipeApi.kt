package com.develoburs.fridgify.model.api
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Header
<<<<<<< Updated upstream
=======
import retrofit2.http.POST
import retrofit2.http.PUT
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

    @POST("recipe-api") // Define the PUT endpoint
    suspend fun addRecipe(
        @Body recipe: createRecipe, // The updated recipe object
        @Header("Authorization") token: String // Optional authorization token
    )
>>>>>>> Stashed changes
}
