package com.example.expensescontrol.model

data class ExpensesUiState(
    val selectedCategory: String = "Grocery",
    var oldExpenses: List<Record> = listOf()
)
{


}
