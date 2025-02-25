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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.type

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

import io.sqlitecloud.ProgressHandler
import io.sqlitecloud.SQLiteCloud
import io.sqlitecloud.SQLiteCloudChannel
import io.sqlitecloud.SQLiteCloudConfig
import io.sqlitecloud.SQLiteCloudError
import io.sqlitecloud.SQLiteCloudResult
import io.sqlitecloud.SQLiteCloudValue

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

import java.nio.file.Path
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.text.toDoubleOrNull
import kotlin.text.toIntOrNull


/**
 * ViewModel to retrieve all items in the Room database.
 */
class MainScreenViewModel(
    private val itemsRepository: ItemsRepository): ViewModel() {

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

    var account by mutableStateOf(Account("", ""))
        private set

    private val rate: Double = 1.0

    var categorySelected by mutableStateOf("")
        private set

    var amountInput by mutableStateOf("")
        private set

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    // Function to convert SQLiteCloudValue to a Kotlin value
    fun SQLiteCloudValue.toKotlinValue(): Any? {
        return when (this) {
            is SQLiteCloudValue.Integer -> this.value.toInt()
            is SQLiteCloudValue.Double -> this.value
            is SQLiteCloudValue.String -> this.value
            is SQLiteCloudValue.Blob -> this.stringValue
            is SQLiteCloudValue.Null -> null
            else -> this.stringValue
        }
    }

    fun selectLatest(context: Context) {
        val configuration = SQLiteCloudConfig.fromString(mainUiState.value.connectionString)
        val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
        val items = mutableListOf<Item>()
        CoroutineScope(Dispatchers.IO).launch {
            sqLiteCloud.connect()
            val r =
                sqLiteCloud.execute("SELECT * FROM items WHERE dateTimeModified = (SELECT MAX(dateTimeModified) FROM items) LIMIT 1")
            Log.d("Select latest1", r.value.toString())
            when (r) {
                is SQLiteCloudResult.Rowset -> {
                    val rowset = r.value

                    val columns = rowset.columns // Assuming this is a List<String>
                    val itemData = mutableMapOf<String, Any?>()
                    for (row in rowset.rows) { // Assuming this is a List<List<SQLiteCloudValue>>
                        for ((index, value) in row.withIndex()) {
                            val columnName = columns[index]
                            itemData[columnName] = value.toKotlinValue()
                        }
                    }
                    val iD = itemData["id"] as? Int ?: 0
                    val dateC = itemData["dateCreated"] as? String ?: ""
                    val dateTimeM = itemData["dateTimeModified"] as? String ?: ""
                    val cat = itemData["category"] as? String ?: ""
                    val amnt = itemData["amount"] as? Double ?: 0.0
                    val curr = itemData["currency"] as? String ?: ""
                    val exchR = itemData["exchRate"] as? Double ?: 0.0
                    val finalAm = itemData["finalAmount"] as? Double ?: 0.0
                    val reg = itemData["regular"] as? Boolean ?: true
                    val userC = itemData["userCreated"] as? String ?: ""
                    val userM = itemData["userModified"] as? String ?: ""
                    val item: Item = Item(
                        id = iD,
                        dateCreated = dateC,
                        dateTimeModified = dateTimeM,
                        category = cat,
                        amount = amnt,
                        currency = curr,
                        exchRate = exchR,
                        finalAmount = finalAm,
                        regular = reg,
                        userCreated = userC,
                        userModified = userM
                    )
                    items.add(item)
                }
                else -> {
                    // Handle other result types or errors
                    println("Unexpected result type: ${r::class.simpleName}")
                }
            }
        }
        Log.d("Downloaded row", items.toString())
    }

    fun uploadDB(dbName: String, dbPath: Path, encryption: String?, progress: ProgressHandler, context: Context){
        val configuration = SQLiteCloudConfig.fromString(mainUiState.value.connectionString)
        val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)

    }

    fun addItemToServer(context: Context, item: Item){
        val configuration = SQLiteCloudConfig.fromString(mainUiState.value.connectionString)
        val sqLiteCloud = SQLiteCloud(appContext = context, config = configuration)
        CoroutineScope(Dispatchers.IO).launch {
                sqLiteCloud.connect()
            val r = sqLiteCloud.execute("INSERT INTO items " +
                    " VALUES (" + "'" +
                    item.id.toString() + "', '" +
                    item.dateCreated + "', '" +
                    item.dateTimeModified + "', '" +
                    item.category + "', '" +
                    item.amount.toString() + "', '" +
                    item.currency + "', '" +
                    item.exchRate.toString() + "', '" +
                    item.finalAmount.toString() + "', '" +
                    item.regular + "', '" +
                    item.userCreated + "', '" +
                    item.userModified + "')"
            )
            Log.d("Added to server", r.value.toString())
        }
    }

    fun populateRegularCategories(items: MutableList<String>){
        categoriesList = items
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

    fun isAccountSetUp(account: Account): Boolean{
        return (account.username != "user1" && account.connectionString != "default")
    }

    fun validateAccountInputData(): Boolean{
        return(
                mainUiState.value.userName.isNotBlank() &&
                mainUiState.value.connectionString.isNotBlank()
                )
    }

    fun updateAccountInfo(acc: Account){
        updateUserName(acc.username)
        updateConnectionString(acc.connectionString)
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