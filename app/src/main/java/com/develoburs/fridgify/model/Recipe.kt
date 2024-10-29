package com.develoburs.fridgify.model

data class Recipe(
    val Name: String,

    val Author: String,

    val Likes: Int,

    val Comments: Int,

    val Images: List<String>
)