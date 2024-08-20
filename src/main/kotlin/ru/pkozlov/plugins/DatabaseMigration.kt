package ru.pkozlov.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import ru.pkozlov.competition.domain.CompetitionTable
import ru.pkozlov.config.DatabaseConfig
import ru.pkozlov.exception.ShemaValidationException
import ru.pkozlov.participant.domain.ParticipantTable

fun Application.configureDatebaseMigration() {
    val config: DatabaseConfig by inject<DatabaseConfig>()

    Flyway
        .configure()
        .dataSource(config.jdbcUrl, config.username, config.password)
        .load()
        .migrate()

    transaction {
        SchemaUtils.statementsRequiredForDatabaseMigration(CompetitionTable, ParticipantTable)
            .takeIf { it.isNotEmpty() }
            ?.let { statements ->
                throw ShemaValidationException("Tables and objects are mismatch. Execute statements:\n${statements.joinToString(separator = "\n")}")
            }
    }
}