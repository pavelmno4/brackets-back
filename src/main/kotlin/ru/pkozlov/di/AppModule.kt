package ru.pkozlov.di

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.serializer
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.pkozlov.config.DatabaseConfig

val appModule: Module = module {
    single<DatabaseConfig> {
        val conf = ConfigFactory.load().getConfig("database")

        @OptIn(ExperimentalSerializationApi::class)
        Hocon.decodeFromConfig(serializer<DatabaseConfig>(), conf)
    }
}