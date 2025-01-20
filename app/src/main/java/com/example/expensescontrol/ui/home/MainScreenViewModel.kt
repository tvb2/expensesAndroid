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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensescontrol.R
import com.example.expensescontrol.model.ExpensesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

import com.example.expensescontrol.data.Item
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.ui.allexp.AllExpensesUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.NumberFormat

/**
 * ViewModel to retrieve all items in the Room database.
 */
class MainScreenViewModel(private val itemsRepository: ItemsRepository): ViewModel() {

    val mainScreenRepoUiState: StateFlow<AllExpensesUiState> =
        itemsRepository.getAllItemsStream().map { AllExpensesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllExpensesUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    val currency = "CAD"
    val user = "tvb2"
    val rate: Double = 1.0

    var categorySelected by mutableStateOf("Select category")
        private set

    var amountInput by mutableStateOf("")
        private set

    private val _mainUiState = MutableStateFlow(ExpensesUiState())
    val mainUiState: StateFlow<ExpensesUiState> = _mainUiState.asStateFlow()

    fun onMainScreenCategorySelected(cat: String) {
        _mainUiState.update { categoryUiState ->
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


    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun addNewExpense(item: Item){
        itemsRepository.insertItem(item)//(itemUiState.itemDetails.toItem())
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            category.isNotBlank() && amountInput.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val dateCreated: String = "Jan 31, 2024",//MMM dd, yyyy
    val dateModified: String = "Jan 31, 2024",
    val category: String = "Grocery",
    val amount: Double = 1.01,
    val currency: String = "CAD",
    val exchRate: Double = 1.0,
    val finalAmount: Double = 1.01,
    val regular: Boolean = true,
    val userCreated: String = "tvb2",
    val userModified: String = "tvb2"
)
/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    category = category,
    dateCreated = dateCreated,
    dateModified = dateModified,
    amount = amount,
    currency = currency,
    exchRate = exchRate,
    finalAmount = finalAmount,
    regular = regular,
    userCreated = userCreated,
    userModified = userModified,
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(amount)
}

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    category = category,
    dateCreated = dateCreated,
    dateModified = dateModified,
    amount = amount,
    currency = currency,
    exchRate = exchRate,
    finalAmount = finalAmount,
    regular = regular,
    userCreated = userCreated,
    userModified = userModified,
)