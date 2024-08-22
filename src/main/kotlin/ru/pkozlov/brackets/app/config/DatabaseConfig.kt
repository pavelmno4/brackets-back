package ru.pkozlov.brackets.app.config

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfig(
    val jdbcUrl: String,
    val driverClassName: String,
    val username: String,
    val password: String,
    val maximumPoolSize: Int,
    val isReadOnly: Boolean,
    val transactionIsolation: String
)