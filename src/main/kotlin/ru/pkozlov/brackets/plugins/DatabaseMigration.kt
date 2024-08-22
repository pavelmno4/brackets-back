package ru.pkozlov.brackets.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.config.DatabaseConfig
import ru.pkozlov.brackets.exception.ShemaValidationException
import ru.pkozlov.brackets.participant.domain.ParticipantTable

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