package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipe-api")
    suspend fun getRecipes(
        @Header("Authorization") token: String
    ): List<Recipe>
}
