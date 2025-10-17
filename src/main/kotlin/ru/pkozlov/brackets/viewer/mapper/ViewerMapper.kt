package ru.pkozlov.brackets.viewer.mapper

import ru.pkozlov.brackets.viewer.domain.Viewer
import ru.pkozlov.brackets.viewer.dto.ViewerDto

fun Viewer.asDto(): ViewerDto = ViewerDto(
    id = id.value,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    phone = phone,
)
