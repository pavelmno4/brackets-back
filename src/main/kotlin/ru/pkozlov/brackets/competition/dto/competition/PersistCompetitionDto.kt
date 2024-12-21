package ru.pkozlov.brackets.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.competition.dto.category.CategoriesByGender
import java.time.LocalDate

@Serializable
data class PersistCompetitionDto(
    val title: String,
    @Serializable(LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val endDate: LocalDate,
    val address: String,
    val imagePath: String,
    val categories: CategoriesByGender
)