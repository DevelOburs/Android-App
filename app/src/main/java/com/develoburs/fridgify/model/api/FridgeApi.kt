package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.Food
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.HTTP

interface FridgeApi {
    data class AddFoodRequest(
        val ingredientIds: List<Int>
    )

    data class DeleteFoodRequest(
        val ingredientIds: List<Int>
    )

    @GET("fridge-api/{userId}/inFridge")
    suspend fun getFood(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Query("nameFilter") nameFilter: String? = null,
        @Query("categoryFilters") categoryFilters: List<String>? = null
    ): List<Food>

    @PUT("fridge-api/{userId}/add")
    suspend fun addFood(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body ingredientIds: AddFoodRequest
    )

    @HTTP(method = "DELETE", path = "fridge-api/{userId}/remove", hasBody = true)
    suspend fun removeFood(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body ingredientIds: DeleteFoodRequest
    )

    @GET("fridge-api/{userId}/notInFridge")
    suspend fun getNotInFridge(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Query("nameFilter") nameFilter: String? = null,
        @Query("categoryFilters") categoryFilters: List<String>? = null
    ): List<Food>



    @GET("fridge-api/categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): List<String>

    @GET("fridge-api/{userId}/inFridge/{ingredientCategory}")
    suspend fun getFoodByCategory(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Path("ingredientCategory") ingredientCategory: String
    ): List<Food>
}


