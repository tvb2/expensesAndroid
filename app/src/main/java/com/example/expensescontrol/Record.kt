package com.example.expensescontrol

import androidx.collection.LongLongMap
import java.util.Date

data class Record(
    val rowId: Long,
    val expDate: Date,
    val createDateTime: Date,
    val modifyDateTime: Date,
    val category: String,
    val amount: Double,
    val currency: String,
    val rate: Double,
    val finalAmount: Double,
    val regular: Boolean,
    val userCreated: String,
    val userLastMod: String
)
