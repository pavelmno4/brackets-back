package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.BigDecimalSerializer
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class PatchParticipantDto(
    val fullName: String? = null,
    @Serializable(LocalDateSerializer::class)
    val birthDate: LocalDate? = null,
    val gender: Gender? = null,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory? = null,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory? = null,
    @Serializable(BigDecimalSerializer::class)
    val weight: BigDecimal? = null,
    val team: String? = null
)