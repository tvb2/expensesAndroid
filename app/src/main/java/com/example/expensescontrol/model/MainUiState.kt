package com.example.expensescontrol.model

import com.example.expensescontrol.data.Item
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import network.chaintech.kmp_date_time_picker.utils.now
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class MainUiState(
    val selectedCategory: String = "Nonsense",

    val amount: Double = 0.0,

    val finalAmount: Double = 0.0,

    val dateCreated: String = LocalDate.now()
        .format(
            LocalDate.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED);
                char(' ');
                dayOfMonth();
                chars(", ");
                year()
            }
        ),

    val dateTimeModified: String =
        OffsetDateTime
        .of(
            LocalDateTime.now(),
            OffsetDateTime.now().offset
        )
        .toString(),

    var oldExpenses: List<Item> = listOf()
)