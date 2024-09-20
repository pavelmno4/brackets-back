package ru.pkozlov.brackets.auth.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.auth.enumeration.Role

@Serializable
data class UserRolesDto(
    val login: String,
    val roles: Set<Role>
)
