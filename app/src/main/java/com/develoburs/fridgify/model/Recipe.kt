package com.develoburs.fridgify.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val Name: String?,

    @SerializedName("userId")
    val Author: String?,

    @SerializedName("userFirstName")
    val AuthorFirstName: String? = "",

    @SerializedName("userLastName")
    val AuthorLastName: String? = "",

    @SerializedName("likeCount")
    val Likes: Int?,

    @SerializedName("commentCount")
    val Comments: Int?,

    @SerializedName("imageUrl")
    val Image: String?,

    @SerializedName("description")
    val instructions: String?,

    @SerializedName("ingredients")
    val ingredients: List<Food>?,

    val comments: List<String>?,

    @SerializedName("calories")
    val calories: Int?,

    @SerializedName("cookingTime")
    val cookingTime: Int?,

    @SerializedName("saveCount")
    val saveCount: Int?
)


data class createRecipe(

    @SerializedName("name")
    val name: String?, // Updated to lowercase for consistency

    @SerializedName("description")
    val description: String?, // Added to match schema


    @SerializedName("userId")
    val userId: String?, // Changed to Long to match integer($int64)

    @SerializedName("userUsername")
    val userUsername: String?, // Added to match schema

    @SerializedName("userFirstName")
    val userFirstName: String? = "",

    @SerializedName("userLastName")
    val userLastName: String? = "",

    @SerializedName("likeCount")
    val likeCount: Int?, // Updated to lowercase for consistency

    @SerializedName("commentCount")
    val commentCount: Int?, // Updated to lowercase for consistency

    @SerializedName("saveCount")
    val saveCount: Int? = 0, // Added to match schema

    @SerializedName("ingredients")
    val ingredients: List<String>?, // List of ingredients

    @SerializedName("imageUrl")
    val imageUrl: String?, // Updated to lowercase for consistency

    @SerializedName("category")
    val category: String?, // Added to match schema

    @SerializedName("calories")
    val calories: Int?, // Added to match schema

    @SerializedName("cookingTime")
    val cookingTime: Int? // Added to match schema
)

// Comment data class to represent each comment
data class Comment(
    @SerializedName("id")
    val id: String? = null, // Matches the JSON key "id"

    @SerializedName("recipeId")
    val recipeId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("createdAt")
    val createdAt: String? = null // Matches the JSON key "createdAt"
)