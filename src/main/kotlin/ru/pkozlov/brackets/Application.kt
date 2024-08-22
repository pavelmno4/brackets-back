package ru.pkozlov.brackets

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.pkozlov.brackets.competition.di.competitionModule
import ru.pkozlov.brackets.di.appModule
import ru.pkozlov.brackets.plugins.configureDatabase
import ru.pkozlov.brackets.plugins.configureDatebaseMigration
import ru.pkozlov.brackets.plugins.configureRouting
import ru.pkozlov.brackets.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule, competitionModule)
    }
    configureSerialization()
    configureDatabase()
    configureDatebaseMigration()
    configureRouting()
}
