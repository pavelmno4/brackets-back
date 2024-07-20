package ru.pkozlov.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.competition.dto.category.Category
import ru.pkozlov.utils.serializer.LocalDateSerializer
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
    val categoryes: List<Category>
)