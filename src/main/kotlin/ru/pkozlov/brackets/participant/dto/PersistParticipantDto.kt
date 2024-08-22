package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.participant.enumeration.Gender

@Serializable
data class PersistParticipantDto(
    val name: String,
    val birthYear: Int,
    val gender: Gender,
    val ageCategory: String,
    val weightCategory: String,
)