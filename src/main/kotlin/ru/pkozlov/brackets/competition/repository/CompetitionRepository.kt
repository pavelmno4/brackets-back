package ru.pkozlov.brackets.competition.repository

import ru.pkozlov.brackets.competition.domain.Competition
import java.time.LocalDate
import java.util.*

interface CompetitionRepository {
    suspend fun findWhereEndDateGreaterOrEq(date: LocalDate): List<Competition>
    suspend fun findWhereEndDateLess(date: LocalDate): List<Competition>
    suspend fun findById(id: UUID): Competition?
    suspend fun create(init: Competition.() -> Unit): Competition
    suspend fun update(id: UUID, action: (it: Competition) -> Unit): Competition?
}