package ru.pkozlov.brackets.competition.repository

import org.jetbrains.exposed.sql.and
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.mapper.asDto
import java.time.LocalDate
import java.util.*

class CompetitionRepositoryImpl : CompetitionRepository {
    override suspend fun findWhereEndDateGreaterOrEq(date: LocalDate): List<CompetitionDto> =
        suspendTransaction {
            Competition
                .find { CompetitionTable.endDate greaterEq date and (CompetitionTable.deleted eq false) }
                .map(Competition::asDto)
        }

    override suspend fun findWhereEndDateLess(date: LocalDate): List<CompetitionDto> =
        suspendTransaction {
            Competition
                .find { CompetitionTable.endDate less date and (CompetitionTable.deleted eq false) }
                .map(Competition::asDto)
        }

    override suspend fun findById(id: UUID): CompetitionDto? =
        suspendTransaction {
            Competition.findById(id)?.asDto()
        }

    override suspend fun create(init: Competition.() -> Unit): CompetitionDto =
        suspendTransaction {
            Competition.new(init).asDto()
        }

    override suspend fun update(id: UUID, action: (Competition) -> Unit): CompetitionDto? =
        suspendTransaction {
            Competition.findByIdAndUpdate(id, action)?.asDto()
        }
}