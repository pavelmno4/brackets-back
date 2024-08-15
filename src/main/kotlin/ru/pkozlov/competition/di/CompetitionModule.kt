package ru.pkozlov.competition.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.competition.repository.CompetitionRepository
import ru.pkozlov.competition.repository.CompetitionRepositoryImpl
import ru.pkozlov.competition.service.CompetitionService

val competitionModule: Module = module {
    singleOf(::CompetitionRepositoryImpl) bind CompetitionRepository::class
    singleOf(::CompetitionService)
}