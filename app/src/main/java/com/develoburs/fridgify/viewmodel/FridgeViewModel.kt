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
    val food: StateFlow<List<Food>> = _food

    init {
        getFoodList()
    }

    private fun getFoodList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val foodList = repository.getFoodList()
                _food.value = foodList
            } catch (e: Exception) {
                Log.e("FridgeViewModel", "Failed to fetch food list", e)
            }
        }
    }
}