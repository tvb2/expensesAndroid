package com.example.expensescontrol.data

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    var account: Account,
    var categories: MutableList<String>
)