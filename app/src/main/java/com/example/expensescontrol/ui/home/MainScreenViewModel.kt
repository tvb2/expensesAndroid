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

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensescontrol.data.Account
import com.example.expensescontrol.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.expensescontrol.data.Item
import com.example.expensescontrol.data.ItemsRepository
import com.example.expensescontrol.ui.allexp.AllExpensesUiState
import com.example.expensescontrol.ui.sync.Sync
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.expensescontrol.workers.SyncWorker
import androidx.work.Data

/**
 * ViewModel to retrieve all items in the Room database.
 */
class MainScreenViewModel(
    private val itemsRepository: ItemsRepository,
    private val sync: Sync,
    private val jsonhandler: JSonHandler,
    private val cont: Context
): ViewModel() {

     private val isAscending: Boolean = false // Change this as needed

    val mainScreenRepoUiState: StateFlow<AllExpensesUiState> =
        itemsRepository.getAllItemsStream().map {items ->
            val sortedItems = listSorted(items, isAscending)

    AllExpensesUiState(sortedItems)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllExpensesUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var categoriesList = mutableListOf("")
    val rate: Double = 1.0
    var categorySelected by mutableStateOf("")
        private set
    var amountInput by mutableStateOf("")
        private set
    var account by mutableStateOf(Account("", ""))
        private set
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()
    val isLocalDBEmpty: Boolean = false

    fun synchronize(){
        if (account.username != "user1" || account.connectionString != "default") {

            sync.setConnection(account.connectionString)
            sync.selectLatest(cont)
            sync.synchronize(isLocalDBEmpty, cont)
        }
    }
    fun populateRegularCategories(){
        categoriesList = jsonhandler.data.categories
    }
    fun updateSelectedCat(newCat: String){
        if (newCat != "Add new category") {
            categorySelected = newCat
            _mainUiState.update { selectedCatUiState ->
                selectedCatUiState.copy(
                    selectedCategory = newCat
                )}
        }else{
            categorySelected = "adding new...."//initiate Add New Category actions
        }
    }
    fun addNewRegularCat(newCat: String){
        categoriesList.add(newCat)
        jsonhandler.updateCategories(categoriesList)
    }
    private fun listSorted(items: List<Item>, isAscending: Boolean = false): List<Item>{
        // Sort items based on the desired column sortedByDescending or sortedBy for ascending order
        val sortedlist = if (isAscending) {
            items.sortedBy { it.dateTimeModified } // Change columnToSortBy to your desired column
        } else {
            items.sortedByDescending { it.dateTimeModified }
        }
        return sortedlist
    }
    fun updateInputAmount(amount: String){
        amountInput = amount
        _mainUiState.update { amountUiState ->
            amountUiState.copy(
                    amount =
                        if(amount.isEmpty()) 0.0
                        else amount.toDouble(),
                    finalAmount =
                        if(amount.isEmpty()) 0.0
                        else amount.toDouble()* rate

            )
        }
}
    fun onCheckedTodayChecked(){
        _mainUiState.update { createdDateUiState ->
            createdDateUiState.copy(
                dateCreated = LocalDate.now()
            )
        }
    }
    fun onCreatedDateChange(newDate: LocalDate){
        _mainUiState.update { createdDateUiState ->
            createdDateUiState.copy(
                dateCreated = newDate
            )
        }
    }
    fun updateDateTimeModified(){
        _mainUiState.update { dateTimeModifiedUiState ->
            dateTimeModifiedUiState.copy(
                dateTimeModified = OffsetDateTime
                    .of(
                        LocalDateTime.now(),
                        OffsetDateTime.now().offset
                    )
                    .toString(),
            )
        }
    }
    fun validateRegularSubmitInput(): Boolean {
        return (
                categoriesList.contains(_mainUiState.value.selectedCategory) &&
                _mainUiState.value.amount != 0.0
                )
    }
    fun validateNonRegularSubmitInput(): Boolean {
        return (
                _mainUiState.value.selectedCategory.isNotEmpty() &&
                        _mainUiState.value.amount != 0.0
                )
    }
    fun validateAccountInputData(): Boolean{
        return(
                mainUiState.value.userName.isNotBlank() &&
                        mainUiState.value.connectionString.isNotBlank()
                )
    }
    fun readAccountInfo(){
        updateUserName(jsonhandler.data.account.username)
        updateConnectionString(jsonhandler.data.account.connectionString)
    }
    fun updateAccount(){
        jsonhandler.updateAccount(account)
    }
    fun updateUserName(user: String) {
        this.account = this.account.copy(
            if (user.length <= 4) user
            else user.subSequence(0, 3).toString()
        )
        _mainUiState.update { userUiState ->
            userUiState.copy(
                userName = this.account.username
            )
        }
    }
    fun updateConnectionString(connection: String) {
        this.account = this.account.copy(connectionString = connection)
        _mainUiState.update { connectionUiState ->
            connectionUiState.copy(
                connectionString = account.connectionString
            )
        }
    }
    fun isAccountSetUp(): Boolean{
        return (jsonhandler.data.account.username != "user1" &&
                jsonhandler.data.account.connectionString != "default")
    }
    suspend fun localDBEmpty(): Boolean {
        return itemsRepository.isLocalEmpty()
    }
    fun scheduleSync() {
        viewModelScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = createInputData()

            val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                5, // repeatInterval
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(cont).enqueue(syncWorkRequest)
        }
    }

    private fun createInputData(): androidx.work.Data {
        var connection = "default"
        var username = "user1"
        if (mainUiState != null) {
            connection = mainUiState.value.connectionString
            username = mainUiState.value.userName
        }
        return Data.Builder()
            .putString("connectionString", connection)
            .putString("username", username)
            .build()
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
        itemsRepository.insertItem(item)
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
    val dateCreated: String = LocalDate.now().toString(),
    val dateTimeModified: String = "Jan 31, 2024",
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
    dateTimeModified = dateTimeModified,
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
    dateTimeModified = dateTimeModified,
    amount = amount,
    currency = currency,
    exchRate = exchRate,
    finalAmount = finalAmount,
    regular = regular,
    userCreated = userCreated,
    userModified = userModified,
)