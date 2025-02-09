package com.example.expensescontrol.ui.stats

data class CategoryStats(
    var category: String = "",
    var catTotal: Double = 0.0,
    var catAverage: Double = 0.0,
    var catNPeriodsAverage: Double = 0.0,
    var catPerCentIncome: Double = 0.0
)
