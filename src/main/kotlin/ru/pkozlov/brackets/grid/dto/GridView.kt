package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.serializer.AgeCategorySerializer
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import ru.pkozlov.brackets.app.utils.serializer.WeightCategorySerializer
import java.util.*

@Serializable
data class GridView(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val gender: Gender,
    @Serializable(AgeCategorySerializer::class)
    val ageCategory: AgeCategory,
    @Serializable(WeightCategorySerializer::class)
    val weightCategory: WeightCategory,
    val nodes: List<NodeView>,
    val edges: List<EdgeView>,
    @Serializable(UUIDSerializer::class)
    val firstPlaceParticipantId: UUID?,
    @Serializable(UUIDSerializer::class)
    val secondPlaceParticipantId: UUID?,
    @Serializable(UUIDSerializer::class)
    val thirdPlaceParticipantId: UUID?,
    val show: Boolean
)
