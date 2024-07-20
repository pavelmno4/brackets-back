package ru.pkozlov.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.inject
import ru.pkozlov.config.DatabaseConfig

fun Application.configureDatabases() {
    val config: DatabaseConfig by inject<DatabaseConfig>()

    HikariConfig()
        .apply {
            jdbcUrl = config.jdbcUrl
            driverClassName = config.driverClassName
            username = config.username
            password = config.password
            maximumPoolSize = config.maximumPoolSize
            isReadOnly = config.isReadOnly
            transactionIsolation = config.transactionIsolation
        }
        .run(::HikariDataSource)
        .run(Database::connect)
}
