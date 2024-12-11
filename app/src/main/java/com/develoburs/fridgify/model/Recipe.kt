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

    val comments: List<String>?


)