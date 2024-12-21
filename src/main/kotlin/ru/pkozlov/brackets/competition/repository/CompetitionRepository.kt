package ru.pkozlov.brackets.competition.repository

import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import java.time.LocalDate
import java.util.*

interface CompetitionRepository {
    suspend fun findWhereEndDateGreaterOrEq(date: LocalDate): List<CompetitionDto>
    suspend fun findWhereEndDateLess(date: LocalDate): List<CompetitionDto>
    suspend fun findById(id: UUID): CompetitionDto?
    suspend fun create(init: Competition.() -> Unit): CompetitionDto
    suspend fun update(id: UUID, action: (it: Competition) -> Unit): CompetitionDto?
}