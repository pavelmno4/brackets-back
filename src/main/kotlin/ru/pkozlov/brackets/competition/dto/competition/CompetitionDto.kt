package ru.pkozlov.brackets.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.competition.dto.category.Category
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
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