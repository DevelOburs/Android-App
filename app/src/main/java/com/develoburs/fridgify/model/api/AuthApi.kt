package com.develoburs.fridgify.model.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val email: String,
    val error: String?
)

interface AuthApi {
    @POST("auth-api/login")
    suspend fun login(
        @Body loginRequest: LoginRequest // Use the LoginRequest class defined earlier
    ): LoginResponse // Use the LoginResponse class defined earlier
}