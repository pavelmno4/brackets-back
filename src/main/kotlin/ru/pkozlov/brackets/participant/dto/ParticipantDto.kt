package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.serializer.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Serializable
data class ParticipantDto(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    @Serializable(LocalDateSerializer::class)
    val birthDate: LocalDate,
    val gender: Gender,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory,
    @Serializable(BigDecimalSerializer::class)
    val weight: BigDecimal?,
    val rank: String?,
    val settlement: String?,
    val coachFullName: String?,
    val team: String
)