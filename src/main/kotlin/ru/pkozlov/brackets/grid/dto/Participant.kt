package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val fullName: String,
    val team: String
)
