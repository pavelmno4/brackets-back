package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.BigDecimalSerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import ru.pkozlov.brackets.app.enumeration.Gender
import java.math.BigDecimal

@Serializable
data class PatchParticipantDto(
    val fullName: String? = null,
    val birthYear: Int? = null,
    val gender: Gender? = null,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory? = null,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory? = null,
    @Serializable(BigDecimalSerializer::class)
    val weight: BigDecimal? = null,
    val team: String? = null
)