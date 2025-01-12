package com.example.expensescontrol.model

import com.example.expensescontrol.data.Item

data class ExpensesUiState(
    val selectedCategory: String = "Grocery",
    var oldExpenses: List<Item> = listOf()
)
{


}
