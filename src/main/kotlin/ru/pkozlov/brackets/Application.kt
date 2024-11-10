package ru.pkozlov.brackets

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.partialcontent.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.pkozlov.brackets.app.di.appModule
import ru.pkozlov.brackets.app.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    install(AutoHeadResponse)
    install(PartialContent)
    configureAuthentication()
    configureSerialization()
    configureDatabase()
    configureDatabaseMigration()
    configureRouting()
}
