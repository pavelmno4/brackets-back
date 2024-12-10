package ru.pkozlov.brackets.grid.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.brackets.grid.repository.GridRepository
import ru.pkozlov.brackets.grid.repository.GridRepositoryImpl
import ru.pkozlov.brackets.grid.service.GridService

val gridModule: Module = module {
    singleOf(::GridRepositoryImpl) bind GridRepository::class
    singleOf(::GridService)
}