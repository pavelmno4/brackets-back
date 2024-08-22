package ru.pkozlov.brackets.app.di

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.pkozlov.brackets.app.config.DatabaseConfig
import ru.pkozlov.brackets.competition.di.competitionModule

val appModule: Module = module {
    includes(competitionModule)

    single<DatabaseConfig> {
        val conf = ConfigFactory.load().getConfig("database")

        @OptIn(ExperimentalSerializationApi::class)
        Hocon.decodeFromConfig(serializer<DatabaseConfig>(), conf)
    }
}