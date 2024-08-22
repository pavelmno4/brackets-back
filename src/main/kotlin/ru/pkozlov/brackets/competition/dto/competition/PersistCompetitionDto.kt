package ru.pkozlov.brackets.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.competition.dto.category.Category
import ru.pkozlov.brackets.utils.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class PersistCompetitionDto(
    val title: String,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val address: String,
    val imagePath: String,
    val categories: List<Category>
)