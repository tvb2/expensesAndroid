/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.expensescontrol.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensescontrol.model.ExpensesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

import com.example.expensescontrol.data.Item
import com.example.expensescontrol.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class MainScreenViewModel(private val itemsRepository: ItemsRepository): ViewModel() {
    /**
     * Holds home ui state. The list of items are retrieved from [ItemsRepository] and mapped to
     * [HomeUiState]
     */
    val mainUiState: StateFlow<MainUiState> =
        itemsRepository.getAllItemsStream().map { MainUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MainUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var categorySelected by mutableStateOf("Select category")
        private set

    var amountInput by mutableStateOf("")
        private set

    private val _expensesUiState = MutableStateFlow(ExpensesUiState())
    val expensesUiState: StateFlow<ExpensesUiState> = _expensesUiState.asStateFlow()

    fun onMainScreenCategorySelected(cat: String) {
    _expensesUiState.update { categoryUiState ->
            categoryUiState.copy(
                selectedCategory = cat,
            )
        }
    }

    fun updateSelectedCat(newCat: String){
        if (newCat != "Add new category") {
            categorySelected = newCat
        }else{
            categorySelected = "adding new...."//initiate Add New Category actions
        }
    }

    fun updateInputAmount(amount: String){
        amountInput = amount
    }


}

/**
 * Ui State for HomeScreen
 */
data class MainUiState(val itemList: List<Item> = listOf())