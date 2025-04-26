package ru.pkozlov.brackets.competition.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import ru.pkozlov.brackets.competition.dto.category.CategoriesByGender
import ru.pkozlov.brackets.competition.enumeration.Stage
import java.time.LocalDate
import java.util.*

@Serializable
data class CompetitionDto(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val title: String,
    @Serializable(LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val endDate: LocalDate,
    val address: String,
    val imagePath: String,
    val categories: CategoriesByGender,
    val stage: Stage,
    val deleted: Boolean
)