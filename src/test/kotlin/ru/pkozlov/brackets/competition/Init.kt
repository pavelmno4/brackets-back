package ru.pkozlov.brackets.competition

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import ru.pkozlov.brackets.app.plugins.configureRouting
import ru.pkozlov.brackets.app.plugins.configureSerialization

fun ApplicationTestBuilder.initPlugins() {
    application {
        configureSerialization()
        configureRouting()
    }

    environment {
        config = MapApplicationConfig()
    }
}

fun ApplicationTestBuilder.initClient() =
    createClient {
        install(ContentNegotiation) {
            json()
        }
    }