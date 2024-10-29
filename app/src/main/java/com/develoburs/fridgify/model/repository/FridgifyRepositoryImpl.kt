package com.develoburs.fridgify.model.repository

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

}