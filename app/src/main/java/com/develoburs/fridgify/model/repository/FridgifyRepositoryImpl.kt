package com.develoburs.fridgify.model.repository

import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.Recipe

class FridgifyRepositoryImpl : FridgifyRepository {

    private val mockRecipes = listOf(
        Recipe(
            Name = "Low-Fat Berry Blue Frozen Dessert",
            Author = "Dancer",
            Likes = 158,
            Comments = 12,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/38/YUeirxMLQaeE1h3v3qnM_229%20berry%20blue%20frzn%20dess.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/38/AFPDDHATWzQ0b1CDpDAT_255%20berry%20blue%20frzn%20dess.jpg",
            )
        ),
        Recipe(
            Name = "Biryani",
            Author = "elly9812",
            Likes = 39,
            Comments = 3,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/39/picM9Mhnw.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/39/picHv4Ocr.jpg"
            )
        ),
        Recipe(
            Name = "Best Lemonade",
            Author = "Stephen Little",
            Likes = 18,
            Comments = 2,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/40/picJ4Sz3N.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/40/pic23FWio.jpg"
            )
        ),
        Recipe(
            Name = "Carina's Tofu-Vegetable Kebabs",
            Author = "Cyclopz",
            Likes = 236,
            Comments = 90,
            Images = listOf(
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/41/picmbLig8.jpg",
                "https://img.sndimg.com/food/image/upload/w_555,h_416,c_fit,fl_progressive,q_95/v1/img/recipes/41/picL02w0s.jpg"
            )
        ),
    )

    override suspend fun getRecipeList(): List<Recipe> {
        return mockRecipes
    }

    private val mockFoods = listOf(
        Food(id = 1, name = "Radish"),
        Food(id = 2, name = "Blueberry"),
        Food(id = 3, name = "Guava"),
        Food(id = 4, name = "Mushroom"),
        Food(id = 5, name = "Kiwi"),
        Food(id = 6, name = "Raspberry"),
        Food(id = 7, name = "Avocado"),
        Food(id = 8, name = "Papaya"),
        Food(id = 9, name = "Zucchini"),
        Food(id = 10, name = "Cabbage"),
        Food(id = 11, name = "Lemon"),
        Food(id = 12, name = "Onion"),
        Food(id = 13, name = "Garlic"),
        Food(id = 14, name = "Carrot"),
        Food(id = 15, name = "Potato"),
        Food(id = 16, name = "Lettuce"),
        Food(id = 17, name = "Corn"),
        Food(id = 18, name = "Chili Pepper"),
        Food(id = 19, name = "Jalapeno"),
        Food(id = 20, name = "Bell Pepper"),
        Food(id = 21, name = "Apple"),
        Food(id = 22, name = "Pear"),
        Food(id = 23, name = "Cucumber"),
        Food(id = 24, name = "Fig"),
        Food(id = 25, name = "Grapes"),
        Food(id = 26, name = "Watermelon"),
        Food(id = 27, name = "Passionfruit"),
        Food(id = 28, name = "Celery"),
        Food(id = 29, name = "Pumpkin"),
        Food(id = 30, name = "Mango"),
        Food(id = 31, name = "Lime"),
        Food(id = 32, name = "Strawberry"),
        Food(id = 33, name = "Orange"),
        Food(id = 34, name = "Blackberry"),
        Food(id = 35, name = "Grapefruit"),
        Food(id = 36, name = "Coconut"),
        Food(id = 37, name = "Honeydew"),
        Food(id = 38, name = "Dragonfruit"),
        Food(id = 39, name = "Tomato"),
        Food(id = 40, name = "Peach"),
        Food(id = 41, name = "Pineapple"),
        Food(id = 42, name = "Pomegranate"),
        Food(id = 43, name = "Cherry"),
        Food(id = 44, name = "Spinach"),
        Food(id = 45, name = "Cantaloupe"),
        Food(id = 46, name = "Lychee"),
        Food(id = 47, name = "Plum"),
        Food(id = 48, name = "Apricot"),
        Food(id = 49, name = "Banana"),
        Food(id = 50, name = "Broccoli")
    )

    override suspend fun getFoodList(): List<Food> {
        return mockFoods
    }

}