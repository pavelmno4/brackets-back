package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import java.time.LocalDate

@Serializable
data class CreateParticipantDto(
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
    val rank: String,
    val settlement: String,
    val coachFullName: String,
    val team: String
)