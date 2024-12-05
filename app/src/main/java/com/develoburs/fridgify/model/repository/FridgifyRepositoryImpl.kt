package com.develoburs.fridgify.model.repository

import android.util.Log
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe
import com.develoburs.fridgify.model.api.RetrofitInstance.api
import android.content.Context

class FridgifyRepositoryImpl : FridgifyRepository {
    //todo update token
    private var token: String = ""
    //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjoxNzMzMzU2OTI1LCJpYXQiOjE3MzMzMjA5MjV9.0XNejo0l46PlNPW6Vcj-BPxhtDMgPZ7TjbP8oNtbAQ8""


    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String {
        return token
    }

    private val mockRecipes = mutableListOf(
        Recipe(
            id = 1,
            Name = "Low-Fat Berry Blue Frozen Dessert",
            Author = "Dancer",
            Likes = 158,
            Comments = 12,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/38/YUeirxMLQaeE1h3v3qnM_229%20berry%20blue%20frzn%20dess.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/38/AFPDDHATWzQ0b1CDpDAT_255%20berry%20blue%20frzn%20dess.jpg",
            ),
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
            id = 2,
            Name = "Biryani",
            Author = "elly9812",
            Likes = 39,
            Comments = 3,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/39/picM9Mhnw.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/39/picHv4Ocr.jpg"
            ),
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
            id = 3,
            Name = "Best Lemonade",
            Author = "Stephen Little",
            Likes = 18,
            Comments = 2,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/40/picJ4Sz3N.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/40/pic23FWio.jpg"
            ),
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
            id = 4,
            Name = "Carina's Tofu-Vegetable Kebabs",
            Author = "Cyclopz",
            Likes = 236,
            Comments = 90,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/41/picmbLig8.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/41/picL02w0s.jpg"
            ),
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

    override suspend fun getRecipeList(): List<Recipe> {
        try {
            // Use getToken function to retrieve the token
            val recipes = api.getRecipes("Bearer ${getToken()}")
            Log.d("Recipe List", recipes.toString())
            return recipes
        } catch (e: Exception) {
            throw Exception("Failed to get recipes, Bearer ${getToken()}", e)
        }
    }

    private val mockFoods = listOf(
        Food(id = 1, name = "Radish", image = "Radish"),
        Food(id = 2, name = "Blueberry", image = "Blueberry"),
        Food(id = 3, name = "Guava", image = "Guava"),
        Food(id = 4, name = "Mushroom", image = "Mushroom"),
        Food(id = 5, name = "Kiwi", image = "Kiwi"),
        Food(id = 6, name = "Raspberry", image = "Raspberry"),
        Food(id = 7, name = "Avocado", image = "Avocado"),
        Food(id = 8, name = "Papaya", image = "Papaya"),
        Food(id = 9, name = "Zucchini", image = "Zucchini"),
        Food(id = 10, name = "Cabbage", image = "Cabbage"),
        Food(id = 11, name = "Lemon", image = "Lemon"),
        Food(id = 12, name = "Onion", image = "Onion"),
        Food(id = 13, name = "Garlic", image = "Garlic"),
        Food(id = 14, name = "Carrot", image = "Carrot"),
        Food(id = 15, name = "Potato", image = "Potato"),
        Food(id = 16, name = "Lettuce", image = "Lettuce"),
        Food(id = 17, name = "Corn", image = "Corn"),
        Food(id = 18, name = "Chili Pepper", image = "Chili Pepper"),
        Food(id = 19, name = "Jalapeno", image = "Jalapeno"),
        Food(id = 20, name = "Bell Pepper", image = "Bell Pepper"),
        Food(id = 21, name = "Apple", image = "Apple"),
        Food(id = 22, name = "Pear", image = "Pear"),
        Food(id = 23, name = "Cucumber", image = "Cucumber"),
        Food(id = 24, name = "Fig", image = "Fig"),
        Food(id = 25, name = "Grapes", image = "Grapes"),
        Food(id = 26, name = "Watermelon", image = "Watermelon"),
        Food(id = 27, name = "Passionfruit", image = "Passionfruit"),
        Food(id = 28, name = "Celery", image = "Celery"),
        Food(id = 29, name = "Pumpkin", image = "Pumpkin"),
        Food(id = 30, name = "Mango", image = "Mango"),
        Food(id = 31, name = "Lime", image = "Lime"),
        Food(id = 32, name = "Strawberry", image = "Strawberry"),
        Food(id = 33, name = "Orange", image = "Orange"),
        Food(id = 34, name = "Blackberry", image = "Blackberry"),
        Food(id = 35, name = "Grapefruit", image = "Grapefruit"),
        Food(id = 36, name = "Coconut", image = "Coconut"),
        Food(id = 37, name = "Honeydew", image = "Honeydew"),
        Food(id = 38, name = "Dragonfruit", image = "Dragonfruit"),
        Food(id = 39, name = "Tomato", image = "Tomato"),
        Food(id = 40, name = "Peach", image = "Peach"),
        Food(id = 41, name = "Pineapple", image = "Pineapple"),
        Food(id = 42, name = "Pomegranate", image = "Pomegranate"),
        Food(id = 43, name = "Cherry", image = "Cherry"),
        Food(id = 44, name = "Spinach", image = "Spinach"),
        Food(id = 45, name = "Cantaloupe", image = "Cantaloupe"),
        Food(id = 46, name = "Lychee", image = "Lychee"),
        Food(id = 47, name = "Plum", image = "Plum"),
        Food(id = 48, name = "Apricot", image = "Apricot"),
        Food(id = 49, name = "Banana", image = "Banana"),
        Food(id = 50, name = "Broccoli", image = "Broccoli")
    )

    fun addRecipe(recipe: Recipe) {
        // Simulate adding the recipe to a backend or database
        mockRecipes.add(recipe) // Add recipe to the local mutable list
    }
    override suspend fun getFoodList(): List<Food> {
        return mockFoods
    }

}