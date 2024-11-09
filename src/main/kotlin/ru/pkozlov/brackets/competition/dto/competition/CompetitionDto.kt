package ru.pkozlov.brackets.competition.dto.competition

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.common.dto.Category
import ru.pkozlov.brackets.app.utils.serializer.LocalDateSerializer
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
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
    val categories: List<Category>,
    val deleted: Boolean
)