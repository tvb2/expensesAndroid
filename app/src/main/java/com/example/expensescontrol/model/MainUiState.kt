package com.example.expensescontrol.model

import android.icu.util.Currency
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

    val dateCreated: LocalDate = LocalDate.now(),

    val currency: String  = "CAD",

    val exchRate: Double = 1.0,

    val userCreated: String = "tvb2",

    val userModified: String = "tvb2",

    val userName: String = "",

    val connectionString: String = "",

    val regular: Boolean = true,

    val dateDisplayed: String = LocalDate.now()
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