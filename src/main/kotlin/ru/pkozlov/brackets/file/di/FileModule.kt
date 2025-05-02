package ru.pkozlov.brackets.file.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.pkozlov.brackets.file.service.FileService

val fileModule: Module = module {
    singleOf(::FileService)
}