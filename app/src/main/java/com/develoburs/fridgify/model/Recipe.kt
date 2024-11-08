package com.develoburs.fridgify.model

data class Recipe(
    val id: String,

    val Name: String,

    val Author: String,

    val Likes: Int,

    val Comments: Int,

    val Images: List<String>,

    val instructions: String,

    val ingredients: List<String>,

    val comments: List<String>


)