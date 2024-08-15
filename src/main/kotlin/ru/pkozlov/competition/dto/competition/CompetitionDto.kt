package ru.pkozlov.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.competition.dto.category.Category
import ru.pkozlov.utils.serializer.LocalDateSerializer
import ru.pkozlov.utils.serializer.UUIDSerializer
import java.time.LocalDate
import java.util.*

@Serializable
data class CompetitionDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val title: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val address: String,
    val imagePath: String,
    val categories: List<Category>,
    val deleted: Boolean
)