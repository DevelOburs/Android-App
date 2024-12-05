package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.Food
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FridgeApi {
    @GET("/fridge-api/{userId}/inFridge")
    suspend fun getFoods(
        @Path("userId") userId: Int,
        @Query("nameFilter") nameFilter: String? = null,
        @Query("categoryFilters") categoryFilters: List<String>? = null
    ): List<Food>
}

