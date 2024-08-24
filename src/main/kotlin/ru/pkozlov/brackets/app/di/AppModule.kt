package ru.pkozlov.brackets.app.di

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.pkozlov.brackets.app.config.DatabaseConfig
import ru.pkozlov.brackets.competition.di.competitionModule
import ru.pkozlov.brackets.participant.di.participantModule
import java.time.LocalDateTime

val appModule: Module = module {
    includes(competitionModule, participantModule)

    single<DatabaseConfig> {
        val conf = ConfigFactory.load().getConfig("database")

        @OptIn(ExperimentalSerializationApi::class)
        Hocon.decodeFromConfig(serializer<DatabaseConfig>(), conf)
    }

    single<() -> LocalDateTime>(named("now")) { { LocalDateTime.now() } }
}