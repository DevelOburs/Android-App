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

    val ingredients: List<String>?,

    val comments: List<String>?,

    val calories: Int?,

<<<<<<< Updated upstream
=======
    val cookingTime: Int?,

    val saveCount: Int?
)

data class createRecipe(
    // Changed to Long to match integer($int64)

    @SerializedName("name")
    val name: String?, // Updated to lowercase for consistency

    @SerializedName("description")
    val description: String?, // Added to match schema

    // Added to match schema

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
>>>>>>> Stashed changes
)