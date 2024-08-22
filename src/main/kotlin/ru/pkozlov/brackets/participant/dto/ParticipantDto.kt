package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.participant.enumeration.Gender
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class ParticipantDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val birthYear: Int,
    val gender: Gender,
    val ageCategory: String,
    val weightCategory: String,
    @Serializable(with = UUIDSerializer::class)
    val competitionId: UUID
)