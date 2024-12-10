package com.develoburs.fridgify.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val Name: String?,

    @SerializedName("userId")
    val Author: String?,

    @SerializedName("likeCount")
    val Likes: Int?,

    @SerializedName("commentCount")
    val Comments: Int?,

    @SerializedName("imageUrl")
    val Image: String?,

    val instructions: String?,

    val ingredients: List<String>?,

    val comments: List<String>?


)