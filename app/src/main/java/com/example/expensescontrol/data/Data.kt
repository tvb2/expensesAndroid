package com.example.expensescontrol.data

import com.example.expensescontrol.data.Account
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    var account: Account,
    var categories: MutableList<String>
)