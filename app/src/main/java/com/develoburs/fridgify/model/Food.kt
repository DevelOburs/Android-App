package com.develoburs.fridgify.model

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val Name: String,

    @SerializedName("category")
    val Category: String,

    @SerializedName("imageUrl")
    val ImageUrl: String,

    )
