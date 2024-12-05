package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.Food
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FridgeApi {
    @GET("fridge-api/{userId}/inFridge")
    suspend fun getFood(
        @Path("userId") userId: Long,  // Path parameter
        @Query("nameFilter") nameFilter: String? = null, // Query parameter (optional)
        @Query("categoryFilters") categoryFilters: List<String>? = null // Query parameter (optional)
    ): List<Food>
}