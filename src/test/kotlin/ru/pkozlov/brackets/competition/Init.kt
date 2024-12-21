package ru.pkozlov.brackets.competition

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.koin.core.KoinApplication
import org.koin.dsl.module
import ru.pkozlov.brackets.app.config.AuthConfig
import ru.pkozlov.brackets.app.plugins.configureAuthentication
import ru.pkozlov.brackets.app.plugins.configureRouting
import ru.pkozlov.brackets.app.plugins.configureSerialization
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
import ru.pkozlov.brackets.competition.service.CompetitionService
import java.time.LocalDateTime

fun KoinApplication.initKoinModules() {
    modules(
        module {
            single<CompetitionRepository> { FakeCompetitionRepository() }
            single<CompetitionService> { CompetitionService(get(), get()) }
            single<AuthConfig> { AuthConfig(signKey = "5848aeb496a92c233038e439cb5f91c2") }
            single<() -> LocalDateTime> { { LocalDateTime.now() } }
        }
    )
}

fun ApplicationTestBuilder.initPlugins() {
    application {
        configureAuthentication()
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