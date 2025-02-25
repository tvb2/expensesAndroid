package com.example.expensescontrol.data

import kotlinx.serialization.Serializable


@Serializable
data class Account(
    var username: String,
    var connectionString: String
)