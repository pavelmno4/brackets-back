package ru.pkozlov.brackets.auth.di

import org.jasypt.util.password.BasicPasswordEncryptor
import org.jasypt.util.password.PasswordEncryptor
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.brackets.auth.repository.UserRepository
import ru.pkozlov.brackets.auth.repository.UserRepositoryImpl
import ru.pkozlov.brackets.auth.service.UserService

val authModule: Module = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::BasicPasswordEncryptor) bind PasswordEncryptor::class
    singleOf(::UserService)
}