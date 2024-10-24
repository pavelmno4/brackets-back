package ru.pkozlov.brackets.app.di

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.pkozlov.brackets.app.config.AuthConfig
import ru.pkozlov.brackets.app.config.DatabaseConfig
import ru.pkozlov.brackets.auth.di.authModule
import ru.pkozlov.brackets.competition.di.competitionModule
import ru.pkozlov.brackets.participant.di.participantModule
import java.time.LocalDateTime

val appModule: Module = module {
    includes(competitionModule, participantModule, authModule)

    @OptIn(ExperimentalSerializationApi::class)
    single<DatabaseConfig> {
        val conf = ConfigFactory.load().getConfig("database")
        Hocon.decodeFromConfig(serializer<DatabaseConfig>(), conf)
    }

    @OptIn(ExperimentalSerializationApi::class)
    single<AuthConfig> {
        val conf = ConfigFactory.load().getConfig("ktor.authentication")
        Hocon.decodeFromConfig(serializer<AuthConfig>(), conf)
    }

    single<() -> LocalDateTime> { { LocalDateTime.now() } }
}