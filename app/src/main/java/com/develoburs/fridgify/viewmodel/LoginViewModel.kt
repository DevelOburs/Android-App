package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.api.AuthApi
import com.develoburs.fridgify.model.api.LoginRequest
import com.develoburs.fridgify.model.api.LoginResponse
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
                    // You might want to log the token to verify
                    Log.d("LoginViewModel", "Token set: ${repository.getToken()}")
                }
                onResult(response)
            } catch (e: Exception) {
                onResult(
                    LoginResponse(
                        token = "",
                        userId = -1,
                        username = "",
                        email = "",
                        error = "Failed to login: ${e.localizedMessage}"
                    )
                )
            }
        }
    }
}
