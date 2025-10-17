package ru.pkozlov.brackets.viewer.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.brackets.viewer.repository.ViewerRepository
import ru.pkozlov.brackets.viewer.repository.ViewerRepositoryImpl
import ru.pkozlov.brackets.viewer.service.ViewerService

val viewerModule: Module = module {
    singleOf(::ViewerRepositoryImpl) bind ViewerRepository::class
    singleOf(::ViewerService)
}