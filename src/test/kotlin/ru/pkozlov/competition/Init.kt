package ru.pkozlov.competition

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import ru.pkozlov.competition.repository.CompetitionRepository
import ru.pkozlov.competition.service.CompetitionService
import ru.pkozlov.plugins.configureRouting
import ru.pkozlov.plugins.configureSerialization

fun ApplicationTestBuilder.initPlugins() {
    install(Koin) {
        modules(
            module {
                single<CompetitionRepository> { FakeRepository() }
                single<CompetitionService> { CompetitionService(get()) }
            }
        )
    }

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