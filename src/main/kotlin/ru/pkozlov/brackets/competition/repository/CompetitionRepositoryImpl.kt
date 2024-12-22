package ru.pkozlov.brackets.competition.repository

import org.jetbrains.exposed.sql.and
import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import java.time.LocalDate
import java.util.*

class CompetitionRepositoryImpl : CompetitionRepository {
    override suspend fun findWhereEndDateGreaterOrEq(date: LocalDate): List<Competition> =
        Competition
            .find { CompetitionTable.endDate greaterEq date and (CompetitionTable.deleted eq false) }
            .toList()

    override suspend fun findWhereEndDateLess(date: LocalDate): List<Competition> =
        Competition
            .find { CompetitionTable.endDate less date and (CompetitionTable.deleted eq false) }
            .toList()

    override suspend fun findById(id: UUID): Competition? =
        Competition.findById(id)


    override suspend fun create(init: Competition.() -> Unit): Competition =
        Competition.new(init)

    override suspend fun update(id: UUID, action: (Competition) -> Unit): Competition? =
        Competition.findByIdAndUpdate(id, action)
}