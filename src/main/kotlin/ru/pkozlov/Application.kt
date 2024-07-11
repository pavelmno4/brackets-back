package ru.pkozlov

import io.ktor.server.application.*
import ru.pkozlov.plugins.configureDatabases
import ru.pkozlov.plugins.configureRouting
import ru.pkozlov.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
}
