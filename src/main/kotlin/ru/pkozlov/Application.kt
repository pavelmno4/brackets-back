package ru.pkozlov

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.pkozlov.di.appModule
import ru.pkozlov.plugins.configureDatabases
import ru.pkozlov.plugins.configureRouting
import ru.pkozlov.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    configureSerialization()
    configureDatabases()
    configureRouting()
}
