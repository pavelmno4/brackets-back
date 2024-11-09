package ru.pkozlov.brackets.grid.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.pkozlov.brackets.grid.service.GridGenerationComponent
import ru.pkozlov.brackets.grid.service.GridService
import ru.pkozlov.brackets.grid.service.TemplateComponent

val gridModule: Module = module {
    singleOf(::GridGenerationComponent)
    singleOf(::TemplateComponent)
    singleOf(::GridService)
}