package com.develoburs.fridgify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl

class RecipeListViewModelFactory(private val navController: NavController,
                                 private val repository: FridgifyRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeListViewModel::class.java)) {
            return RecipeListViewModel(navController,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
