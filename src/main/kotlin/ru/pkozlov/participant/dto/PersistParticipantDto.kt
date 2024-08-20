package ru.pkozlov.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.participant.enumeration.Gender

@Serializable
data class PersistParticipantDto(
    val name: String,
    val birthYear: Int,
    val gender: Gender,
    val ageCategory: String,
    val weightCategory: String,
)