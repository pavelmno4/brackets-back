package ru.pkozlov.brackets.competition.repository

import org.jetbrains.exposed.sql.and
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.mapper.asDto
import java.time.LocalDateTime
import java.util.*

class CompetitionRepositoryImpl(
    val now: () -> LocalDateTime
) : CompetitionRepository {
    override suspend fun findUpcoming(): List<CompetitionDto> =
        suspendTransaction {
            Competition
                .find { CompetitionTable.endDate greaterEq now().toLocalDate() and (CompetitionTable.deleted eq false) }
                .map(Competition::asDto)
        }

    override suspend fun findPast(): List<CompetitionDto> =
        suspendTransaction {
            Competition
                .find { CompetitionTable.endDate less now().toLocalDate() and (CompetitionTable.deleted eq false) }
                .map(Competition::asDto)
        }

    override suspend fun findById(id: UUID): CompetitionDto? =
        suspendTransaction {
            Competition.findById(id)?.asDto()
        }

    override suspend fun create(competition: PersistCompetitionDto): CompetitionDto =
        suspendTransaction {
            Competition.new {
                title = competition.title
                startDate = competition.startDate
                endDate = competition.endDate
                address = competition.address
                imagePath = competition.imagePath
                categories = competition.categories
                deleted = false
                createdAt = now()
                updatedAt = now()
            }.asDto()
        }

    override suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto? =
        suspendTransaction {
            Competition.findByIdAndUpdate(id) { competition ->
                competition.title = updatedCompetition.title
                competition.startDate = updatedCompetition.startDate
                competition.endDate = updatedCompetition.endDate
                competition.address = updatedCompetition.address
                competition.imagePath = updatedCompetition.imagePath
                competition.categories = updatedCompetition.categories
                competition.updatedAt = now()
            }?.asDto()
        }

    override suspend fun delete(id: UUID): CompetitionDto? =
        suspendTransaction {
            Competition.findByIdAndUpdate(id) { competition ->
                competition.deleted = true
                competition.updatedAt = now()
            }?.asDto()
        }
}