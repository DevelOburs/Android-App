package com.develoburs.fridgify.model

import com.google.gson.annotations.SerializedName

data class Food(

    @SerializedName("name")
    val name: String, // Kotlin'de değişken isimleri küçük harfle başlar.

    @SerializedName("category")
    val category: String,

    @SerializedName("ImageUrl")
    val imageUrl: String // Alan isimlerinde PascalCase yerine camelCase kullanımı önerilir.
)
