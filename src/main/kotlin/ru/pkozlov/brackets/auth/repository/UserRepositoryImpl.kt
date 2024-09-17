package ru.pkozlov.brackets.auth.repository

import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.auth.domain.User
import ru.pkozlov.brackets.auth.domain.UserTable
import ru.pkozlov.brackets.auth.dto.UserDto
import ru.pkozlov.brackets.auth.mapper.asDto

class UserRepositoryImpl : UserRepository {
    override suspend fun findByLogin(login: String): UserDto? =
        suspendTransaction {
            User.find { UserTable.login eq login }
                .singleOrNull()
                ?.asDto()
        }
}