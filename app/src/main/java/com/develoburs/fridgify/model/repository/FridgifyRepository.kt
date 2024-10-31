package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

interface FridgifyRepository {

    suspend fun getRecipeList(): List<Recipe>
    suspend fun getFoodList(): List<Food>

}