package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

interface FridgifyRepository {

    suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe>

    suspend fun getRecipeById(id: String): Recipe

    suspend fun getFoodList(): List<Food>

}