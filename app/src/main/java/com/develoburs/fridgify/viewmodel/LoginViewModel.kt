package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.api.AuthApi
import com.develoburs.fridgify.model.api.ChangePasswordRequest
import com.develoburs.fridgify.model.api.ChangePasswordResponse
import com.develoburs.fridgify.model.api.LoginRequest
import com.develoburs.fridgify.model.api.LoginResponse
import com.develoburs.fridgify.model.api.RegisterRequest
import com.develoburs.fridgify.model.api.RegisterResponse
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authApi: AuthApi,
    private val repository: FridgifyRepositoryImpl
) : ViewModel() {

    fun login(username: String, email: String, password: String, onResult: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = authApi.login(
                    loginRequest = LoginRequest(username, email, password)
                )
                if (response.error.isNullOrEmpty()) {
                    // Save the token in the repository
                    repository.setToken(response.token)
                    repository.setUserID(response.userId)
                    repository.setUserName(response.username)
                    repository.setUserFirstName(response.firstName)
                    repository.setUserLastName(response.lastName)
                    // You might want to log the token to verify
                    Log.d("LoginViewModel", "Token set: ${repository.getToken()}")
                    Log.d("LoginViewModel","Welcome, ${response.username} (ID: ${response.userId})")
                }
                onResult(response)
            } catch (e: Exception) {
                onResult(
                    LoginResponse(
                        token = "",
                        userId = -1,
                        username = "",
                        firstName = "",
                        lastName = "",
                        email = "",
                        error = "Failed to login: ${e.localizedMessage}"
                    )
                )
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onResult: (RegisterResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = authApi.register(
                    registerRequest = RegisterRequest(username, email, password, firstName, lastName)
                )
                onResult(response)
            } catch (e: Exception) {
                onResult(
                    RegisterResponse(
                        userId = -1,
                        username = "",
                        email = "",
                        firstName = "",
                        lastName = ""
                    ) // Handle error appropriately
                )
                Log.e("LoginViewModel", "Failed to register: ${e.localizedMessage}")
            }
        }
    }

    fun changePassword(
        username: String,
        password: String,
        newPassword: String,
        onResult: (ChangePasswordResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = authApi.changePassword(
                    changePasswordRequest = ChangePasswordRequest(username, password, newPassword),
                    token = "Bearer ${repository.getToken()}"
                )
                onResult(response) // Pass the successful response to the callback
            } catch (e: Exception) {
                onResult(
                    ChangePasswordResponse(
                        userId = -1,
                        username = "",
                        email = "",
                        firstName = "",
                        lastName = ""
                    ) // Handle error by returning an empty response or a meaningful fallback
                )
                Log.e("UserViewModel", "Failed to change password: ${e.localizedMessage}")
            }
        }
    }

}
