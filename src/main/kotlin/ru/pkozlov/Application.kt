package ru.pkozlov

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.pkozlov.competition.di.competitionModule
import ru.pkozlov.di.appModule
import ru.pkozlov.plugins.configureDatabase
import ru.pkozlov.plugins.configureDatebaseMigration
import ru.pkozlov.plugins.configureRouting
import ru.pkozlov.plugins.configureSerialization

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
