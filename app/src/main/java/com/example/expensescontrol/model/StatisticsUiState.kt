package com.example.expensescontrol.model

import com.example.expensescontrol.ui.stats.CategoryStats
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class StatisticsUiState(
    //General values
    val selectedCategory: String = "Nonsense",
    val startDateTime: String =
        OffsetDateTime
            .of(
                LocalDateTime.now(),
                OffsetDateTime.now().offset
            )
            .toString(),
    val periods: MutableMap<String, LocalDate> = mutableMapOf(),


    //Total values
    val total: Double = 0.0,
    val totalRegular: Double = 0.0,
    val totalNonRegular: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalBalance: Double = 0.0,

    //Period values
    val thisPeriodTotal: Double = 0.0,
    val thisPeriodRegular: Double = 0.0,
    val thisPeriodNonRegular: Double = 0.0,
    val thisPeriodIncome: Double = 0.0,
    val thisPeriodBalance: Double = 0.0,

    //Average values
    val averageThisPeriod: Double = 0.0,
    val averageRegular: Double = 0.0,
    val averageNonRegular: Double = 0.0,
    val averageIncome: Double = 0.0,

    //Category values
    val selectedCategoryAvg: Int = 0,
    val selectedCategoryTotal: Double = 0.0,
    val selectedCategoryTotalThisPeriod: Double = 0.0,
    val categoryStats: List<CategoryStats> = listOf()
)