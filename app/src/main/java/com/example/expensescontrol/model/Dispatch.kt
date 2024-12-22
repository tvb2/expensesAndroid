package com.example.expensescontrol.model

import android.content.res.Resources
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.expensescontrol.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.expensescontrol.DBHandler
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class Dispatch: ViewModel() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPreviousExpenses(): MutableList<com.example.expensescontrol.Record>{
//        val date = Date(dateString = 2024-12-22 T 10:18:15.311Z)
        var date = Date(2024, 12,  17)
        val rec: com.example.expensescontrol.Record = com.example.expensescontrol.Record(
            rowId = 1,
            expDate = date,
            createDateTime = date,
            modifyDateTime = date,
            category = "Grocery",
            amount = 53.6,
            currency = "CAD",
            rate = 1.0,
            finalAmount = 53.6,
            regular = true,
            userCreated = "Bogdan",
            userLastMod = "Bogdan"
        )
        var oldExp: MutableList<com.example.expensescontrol.Record> = mutableListOf(rec)
        repeat (500){
            oldExp.add(rec)
        }
        return oldExp
    }
}