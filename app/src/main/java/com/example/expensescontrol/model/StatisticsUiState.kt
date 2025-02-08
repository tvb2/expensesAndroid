package com.example.expensescontrol.model

import com.example.expensescontrol.data.Item
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class StatisticsUiState(
    val selectedCategory: String = "Nonsense",

    val selectedCategoryAvg: Int = 0,

    val selectedCategoryTotalThisPeriod: Double = 0.0,

    val selectedCategoryTotal: Double = 0.0,

    val totalThisPeriod: Double = 0.0,
    val regularThisPeriod: Double = 0.0,
    val nonRegularThisPeriod: Double = 0.0,
    val averageThisPeriod: Double = 0.0,
    val incomeThisPeriod: Double = 0.0,
    val balanceThisPeriod: Double = 0.0,

    val startDateTime: String =
        OffsetDateTime
        .of(
            LocalDateTime.now(),
            OffsetDateTime.now().offset
        )
        .toString(),

)