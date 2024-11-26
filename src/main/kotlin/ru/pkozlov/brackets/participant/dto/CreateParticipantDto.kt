package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import ru.pkozlov.brackets.app.enumeration.Gender

@Serializable
data class CreateParticipantDto(
    val fullName: String,
    val birthYear: Int,
    val gender: Gender,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory,
    val team: String
)