package com.develoburs.fridgify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoburs.fridgify.model.Food
import com.develoburs.fridgify.model.repository.FridgifyRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FridgeViewModel : ViewModel() {
    private val repository = FridgifyRepositoryImpl()
    private val _food = MutableStateFlow<List<Food>>(emptyList())
    val food: StateFlow<List<Food>> get() = _food
    private var userId: Int = 0
    fun setUserId(id: Int) {
        userId = id
    }

    fun fetchFoods(userId: Int) {
        if (userId <= 0) {
            Log.e("FridgeViewModel", "Invalid userId: $userId")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getFoodList(userId)
                _food.value = foodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Error fetching foods: ${e.message}", e)
            }
        }
    }

}