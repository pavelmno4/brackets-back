package ru.pkozlov.brackets.participant.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.BigDecimalSerializer
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import ru.pkozlov.brackets.app.enumeration.Gender
import java.math.BigDecimal
import java.util.*

@Serializable
data class ParticipantDto(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val fullName: String,
    val birthYear: Int,
    val gender: Gender,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory,
    @Serializable(BigDecimalSerializer::class)
    val weight: BigDecimal?,
    val team: String
)