package com.develoburs.fridgify.model.api

import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import com.develoburs.fridgify.viewmodel.LoginViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://ec2-51-21-135-63.eu-north-1.compute.amazonaws.com:8080/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RecipeApi by lazy {
        retrofit.create(RecipeApi::class.java)
    }

    val authapi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }


}