package com.develoburs.fridgify.model.repository

import android.util.Log
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.api.RetrofitInstance.api
import com.develoburs.fridgify.model.api.RetrofitInstance.fridgeapi

import android.content.Context
import com.develoburs.fridgify.model.api.FridgeApi

class FridgifyRepositoryImpl : FridgifyRepository {

    private var token: String = ""

    private var userId: Int = 0
    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String {
        return token
    }
    fun setUserID(UserID: Int) {
        userId = UserID
    }

    fun getUserID(): Int {
        return userId
    }


    private val mockRecipes = mutableListOf(
        Recipe(
            id = "1",
            Name = "Low-Fat Berry Blue Frozen Dessert",
            Author = "Dancer",
            AuthorFirstName = "",
            AuthorLastName = "",
            Likes = 158,
            Comments = 12,
            Image =
            "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/38/YUeirxMLQaeE1h3v3qnM_229%20berry%20blue%20frzn%20dess.jpg",
            instructions = "Mix all ingredients and freeze for 2 hours.",
            ingredients = listOf(
                "1 cup frozen blueberries",
                "1/2 cup low-fat yogurt",
                "1 tablespoon honey",
                "1/2 teaspoon vanilla extract"
            ),
            comments = listOf(
                "Delicious and easy to make! My family loved it.",
                "Perfect summer dessert, very refreshing!",
                "I added a bit of lemon zest, and it was amazing."
            )
        ),
        Recipe(
            id = "2",
            Name = "Biryani",
            Author = "elly9812",
            AuthorFirstName = "",
            AuthorLastName = "",
            Likes = 39,
            Comments = 3,
            Image =
            "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/39/picM9Mhnw.jpg",
            instructions = "Cook the rice and mix with spices and meat.",
            ingredients = listOf(
                "2 cups basmati rice",
                "1 lb chicken or beef",
                "1 cup yogurt",
                "1 tablespoon biryani spice mix",
                "1 large onion, sliced",
                "1/4 cup cilantro, chopped"
            ),
            comments = listOf(
                "The flavors are incredible! Will make again.",
                "Took some time, but worth the effort.",
                "Added extra spice, and it was perfect for our taste."
            )
        ),
        Recipe(
            id = "3",
            Name = "Best Lemonade",
            Author = "Stephen Little",
            AuthorFirstName = "",
            AuthorLastName = "",
            Likes = 18,
            Comments = 2,
            Image =
            "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/40/picJ4Sz3N.jpg",
            instructions = "Mix lemon juice with water and sugar. Serve cold.",
            ingredients = listOf(
                "4 cups water",
                "1 cup fresh lemon juice",
                "1/2 cup sugar",
                "Ice cubes",
                "Lemon slices for garnish"
            ),
            comments = listOf(
                "Super refreshing and easy to make.",
                "Perfect for a hot day, loved it!"
            )
        ),
        Recipe(
            id = "4",
            Name = "Carina's Tofu-Vegetable Kebabs",
            Author = "Cyclopz",
            AuthorFirstName = "",
            AuthorLastName = "",
            Likes = 236,
            Comments = 90,
            Image = "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/41/picmbLig8.jpg",
            instructions = "Marinate tofu and vegetables, skewer them, and grill until cooked.",
            ingredients = listOf(
                "1 cup firm tofu, cubed",
                "1 bell pepper, chopped",
                "1 zucchini, sliced",
                "1/2 cup mushrooms, halved",
                "2 tablespoons olive oil",
                "1 tablespoon soy sauce",
                "1 teaspoon garlic powder",
                "Salt and pepper to taste"
            ),
            comments = listOf(
                "These were a hit at our BBQ!",
                "I used tempeh instead of tofu, and it turned out great.",
                "The marinade was delicious; will make again."
            )
        ),
    )

    override suspend fun getRecipeList(limit: Int, pageNumber: Int): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getRecipes(
                token = "Bearer ${getToken()}",
                limit = limit,
                pageNumber = pageNumber
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getUserRecipeList(): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getRecipesByUserId(
                userId = getUserID(),
                token = "Bearer ${getToken()}"
            )
//            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getRecipeById(id: String): Recipe {
        return try {
            val token = "Bearer ${getToken()}" // Dynamically fetch the token
            val recipe = api.getRecipeById(id, token) // Pass the token in the API call
            Log.d("Recipe", "Fetched recipe: $recipe")
            recipe
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to get recipe detail for ID: $id", e)
            throw Exception("Failed to get recipe detail")
        }
    }


    /*
        private val mockFoods = listOf(
            Food(
                id = 1, Name = "Radish", ImageUrl = "Radish", Category =
            ),
            Food(id = 2, Name = "Blueberry", ImageUrl = "Blueberry"),
            Food(id = 3, Name = "Guava", ImageUrl = "Guava"),
            Food(id = 4, Name = "Mushroom", ImageUrl = "Mushroom"),
            Food(id = 5, Name = "Kiwi", ImageUrl = "Kiwi"),
            Food(id = 6, Name = "Raspberry", ImageUrl = "Raspberry"),
            Food(id = 7, Name = "Avocado", ImageUrl = "Avocado"),
            Food(id = 8, Name = "Papaya", ImageUrl = "Papaya"),
            Food(id = 9, Name = "Zucchini", ImageUrl = "Zucchini"),

        )*/

    fun addRecipe(recipe: Recipe) {
        // Simulate adding the recipe to a backend or database
        mockRecipes.add(recipe) // Add recipe to the local mutable list
    }

    override suspend fun getRecipeIngredients(id: String): List<Food> {
        try {
            // Use getToken function to retrieve the token
            val foods = api.getRecipeIngredients(id, token)
            Log.d("Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            throw Exception("Failed to get foods, Bearer ${getToken()}", e)
        }
    }

    override suspend fun getFoodList(category: String?): List<Food> {
        try {
            val foods = fridgeapi.getFood(
                token = "Bearer ${getToken()}",
                userId = getUserID(),
                category = category?.let { listOf(it) } // Pass category as a list if not null
            )
            Log.d("Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            throw Exception("Failed to fetch food list", e)
        }
    }

    override suspend fun getNotInFridge(nameFilter: String?, categoryFilters: List<String>?): List<Food> {
        try {
            val foods = fridgeapi.getNotInFridge(
                "Bearer ${getToken()}",
                userId = getUserID(),
                nameFilter = nameFilter,
                categoryFilters = categoryFilters
            )
            Log.d("Not In Fridge Food List", foods.toString())
            return foods
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to get not in fridge foods", e)
            throw Exception("Failed to get not in fridge foods", e)
        }
    }

    override suspend fun addFood(ingredientIds: List<Int>) {
        try {
            val request = FridgeApi.AddFoodRequest(ingredientIds)
            fridgeapi.addFood(
                token = "Bearer ${getToken()}",
                userId = getUserID(),
                ingredientIds = request
            )
            Log.d("FridgifyRepositoryImpl", "Food added successfully")
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to add food", e)
            throw Exception("Failed to add food: ${e.message}", e)
        }
    }


    override suspend fun removeFood(ingredientIds:  List<Int>) {
        try {
            val request = FridgeApi.DeleteFoodRequest(ingredientIds)
            fridgeapi.removeFood(
                "Bearer ${getToken()}",
                userId = getUserID(),
                ingredientIds = request
            )
            Log.d("FridgifyRepositoryImpl", "Food removed successfully")
        } catch (e: Exception) {
            Log.e("FridgifyRepositoryImpl", "Failed to remove food", e)
            throw Exception("Failed to remove food", e)
        }
    }






}