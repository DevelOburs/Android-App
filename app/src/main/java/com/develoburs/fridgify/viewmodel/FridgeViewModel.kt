package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    private val _allfood = MutableStateFlow<List<Food>>(emptyList())
    val allfood: StateFlow<List<Food>> = _allfood

    private val _notInFridgeFood = MutableStateFlow<List<Food>>(emptyList())
    val notInFridgeFood: StateFlow<List<Food>> = _notInFridgeFood

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    val selectedFoods = mutableStateListOf<Food>()
    private val _isLoading = MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getFoodList()
        getAllFoodList()
    }

    fun getAllFoodList() {
        if (_isLoading.value ) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getAllFoodList()
                _allfood.value = foodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch food list", e)
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun getFoodList(category: String? = null) {
        if (_isLoading.value ) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getFoodList(category)
                _food.value = foodList
                Log.d("API_CALL", "Category sent: $category")
                Log.d("API_RESPONSE", "Response: $_food")
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch food list", e)
            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun getNotInFridgeFood( category: String? = null) {
        if (_isLoading.value ) return
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notInFridgeFoodList = repository.getNotInFridge(category)
                _notInFridgeFood.value = notInFridgeFoodList
                Log.d("API_CALL", "Category sent: $category")
                Log.d("API_RESPONSE", "Response: $_notInFridgeFood")
            } catch (e: Exception) {
                Log.d("API_CALL", "Category sent: $category")
                Log.d("API_RESPONSE", "Response: $_notInFridgeFood")
                Log.e("FridgeViewModel", "Failed to fetch not in fridge food list", e)
            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun addFood(ingredientIds: List<Int>) {

        if (ingredientIds.isEmpty()) {
            Log.w("FridgeViewModel", "No ingredients selected to add")
            return
        }
        if (_isLoading.value ) return
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addFood(ingredientIds)
                getFoodList()
                Log.d("FridgeViewModel", "Food added successfully")

            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to add food", e)
            }
            finally {
                _isLoading.value = false
                getFoodList()

            }
        }
    }



    fun removeFood(ingredientIds:  List<Int>) {
        if (_isLoading.value ) return
        _isLoading.value = true
        if (ingredientIds.isEmpty()) {
            Log.w("FridgeViewModel", "No ingredients selected to delete")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeFood(ingredientIds)
                getFoodList()
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Delete food", e)
            }finally {
                _isLoading.value = false
                getFoodList()
            }
        }
    }






}