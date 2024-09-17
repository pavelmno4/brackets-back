package ru.pkozlov.brackets.auth.repository

import ru.pkozlov.brackets.auth.dto.UserDto

interface UserRepository {
    suspend fun findByLogin(login: String): UserDto?
}