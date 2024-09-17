package ru.pkozlov.brackets.auth.mapper

import ru.pkozlov.brackets.auth.domain.User
import ru.pkozlov.brackets.auth.dto.UserDto

fun User.asDto(): UserDto = UserDto(
    login = login,
    password = password,
    roles = roles.toSet()
)