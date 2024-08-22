package ru.pkozlov.brackets.app.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.app.config.DatabaseConfig
import ru.pkozlov.brackets.app.exception.SchemaValidationException
import ru.pkozlov.brackets.participant.domain.ParticipantTable

fun Application.configureDatabaseMigration() {
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
                throw SchemaValidationException("Tables and objects are mismatch. Execute statements:\n${statements.joinToString(separator = "\n")}")
            }
    }
}