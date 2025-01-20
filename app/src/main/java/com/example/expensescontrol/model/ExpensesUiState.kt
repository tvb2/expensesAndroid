package com.example.expensescontrol.model

import com.example.expensescontrol.data.Item

data class ExpensesUiState(
    val selectedCategory: String = "Grocery",
    val createdDate: String = "2000-01-31",
    val modifiedDated: String = "2000-01-31",
    var oldExpenses: List<Item> = listOf()
)