package com.develoburs.fridgify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.develoburs.fridgify.model.api.AuthApi
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl

class LoginViewModelFactory(
    private val authApi: AuthApi,
    private val repository: FridgifyRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authApi, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
