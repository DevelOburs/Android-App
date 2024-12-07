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

// Data class for the registration request
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

// Data class for the registration response
data class RegisterResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
interface AuthApi {
    @POST("auth-api/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("user-api/register") // Define the endpoint for registration
    suspend fun register(
        @Body registerRequest: RegisterRequest // Use the RegisterRequest class
    ): RegisterResponse // Use the RegisterResponse class
}