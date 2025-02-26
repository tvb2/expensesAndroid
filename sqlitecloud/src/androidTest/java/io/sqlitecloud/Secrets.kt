package io.sqlitecloud

import kotlinx.serialization.Serializable

@Serializable
data class Secrets(
    val hostname: String,
    val username: String,
    val password: String,
    val apiKey: String
)
