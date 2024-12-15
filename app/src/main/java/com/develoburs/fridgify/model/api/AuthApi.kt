package com.develoburs.fridgify.model.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
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
data class ChangePasswordResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
data class ChangePasswordRequest(
    val username: String,
    val password: String,
    val newPassword: String
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

    @PUT("user-api/changePassword") // Define the PUT endpoint
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): ChangePasswordResponse // Define the response type

}