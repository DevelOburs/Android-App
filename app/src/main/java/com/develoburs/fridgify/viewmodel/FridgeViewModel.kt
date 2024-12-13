package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FridgeViewModel(private val repository: FridgifyRepositoryImpl) : ViewModel() {
    private val _food = MutableStateFlow<List<Food>>(emptyList())
    val food: StateFlow<List<Food>> = _food

    private val _notInFridgeFood = MutableStateFlow<List<Food>>(emptyList())
    val notInFridgeFood: StateFlow<List<Food>> = _notInFridgeFood

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    val selectedFoods = mutableStateListOf<Food>()

    init {
        getFoodList()
        getCategories()
    }


    fun getFoodList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getFoodList()
                _food.value = foodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch food list", e)
            }
        }
    }
    fun getNotInFridgeFood(nameFilter: String? = null, categoryFilters: List<String>? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notInFridgeFoodList = repository.getNotInFridge(nameFilter, categoryFilters)
                _notInFridgeFood.value = notInFridgeFoodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch not in fridge food list", e)
            }
        }
    }
    fun addFood(ingredientIds: List<Int>) {
        if (ingredientIds.isEmpty()) {
            Log.w("FridgeViewModel", "No ingredients selected to add")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addFood(ingredientIds)
                Log.d("FridgeViewModel", "Food added successfully")
                getFoodList() // Listeyi güncelle
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to add food", e)
            }
        }
    }



    fun removeFood(ingredientIds:  List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeFood(ingredientIds)
                getFoodList() // Refresh the food list after removing
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to remove food", e)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = repository.getCategories()
                _categories.value = categoryList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch categories", e)
            }
        }
    }

    fun getFoodByCategory(ingredientCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getFoodByCategory(ingredientCategory)
                _food.value = foodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch food by category", e)
            }
        }
    }



}
