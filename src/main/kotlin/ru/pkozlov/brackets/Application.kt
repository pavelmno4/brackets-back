package ru.pkozlov.brackets

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.pkozlov.brackets.app.di.appModule
import ru.pkozlov.brackets.app.plugins.configureDatabase
import ru.pkozlov.brackets.app.plugins.configureDatabaseMigration
import ru.pkozlov.brackets.app.plugins.configureRouting
import ru.pkozlov.brackets.app.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureSerialization()
    configureDatabase()
    configureDatabaseMigration()
    configureRouting()
}
