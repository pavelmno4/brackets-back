package ru.pkozlov.brackets.competition.dto.category

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer

@Serializable
data class Category(
    @Serializable(AgeCategorySerializer::class)
    val yearRange: AgeCategory,
    val weights: Set<@Serializable(WeightCategorySerializer::class) WeightCategory>
)