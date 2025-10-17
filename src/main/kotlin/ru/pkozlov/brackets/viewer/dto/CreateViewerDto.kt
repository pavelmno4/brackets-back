package ru.pkozlov.brackets.viewer.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateViewerDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phone: String,
)
