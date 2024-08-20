package ru.pkozlov.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.participant.enumeration.Gender
import ru.pkozlov.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class ParticipantDto(
    val name: String,
    val birthYear: Int,
    val gender: Gender,
    val ageCategory: String,
    val weightCategory: String,
    @Serializable(with = UUIDSerializer::class)
    val competitionId: UUID
)