package ru.pkozlov.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.koin.ktor.ext.inject
import ru.pkozlov.config.DatabaseConfig

fun Application.configureDatebaseMigration() {
    val config: DatabaseConfig by inject<DatabaseConfig>()

    Flyway
        .configure()
        .dataSource(config.jdbcUrl, config.username, config.password)
        .load()
        .migrate()
}