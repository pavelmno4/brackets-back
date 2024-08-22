package ru.pkozlov.brackets.competition.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
import ru.pkozlov.brackets.competition.repository.CompetitionRepositoryImpl
import ru.pkozlov.brackets.competition.service.CompetitionService

val competitionModule: Module = module {
    singleOf(::CompetitionRepositoryImpl) bind CompetitionRepository::class
    singleOf(::CompetitionService)
}