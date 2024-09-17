package ru.pkozlov.brackets.auth.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.auth.enumeration.Role

@Serializable
data class UserDto(
    val login: String,
    val password: String,
    val roles: Set<Role>
)
