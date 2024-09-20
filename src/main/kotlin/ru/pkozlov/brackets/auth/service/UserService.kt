package ru.pkozlov.brackets.auth.service

import io.ktor.server.auth.*
import org.jasypt.util.password.PasswordEncryptor
import ru.pkozlov.brackets.auth.dto.UserRolesDto
import ru.pkozlov.brackets.auth.repository.UserRepository

class UserService(
    private val userRepository: UserRepository,
    private val passwordEncryptor: PasswordEncryptor
) {
    suspend fun authenticate(credential: UserPasswordCredential): UserIdPrincipal? =
        userRepository.findByLogin(credential.name)
            ?.takeIf { user -> passwordEncryptor.checkPassword(credential.password, user.password) }
            ?.run { UserIdPrincipal(credential.name) }

    suspend fun findUser(login: String): UserRolesDto? =
        userRepository.findByLogin(login)?.run {
            UserRolesDto(
                login = login,
                roles = roles
            )
        }
}