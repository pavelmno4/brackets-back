package ru.pkozlov.brackets.competition.repository

import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.mapper.asDto
import ru.pkozlov.brackets.app.utils.suspendTransaction
import java.time.LocalDateTime
import java.util.*

class CompetitionRepositoryImpl : CompetitionRepository {
    override suspend fun findAll(): List<CompetitionDto> =
        suspendTransaction {
            Competition.all().map(Competition::asDto)
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
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
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
                competition.updatedAt = LocalDateTime.now()
            }?.asDto()
        }

    override suspend fun delete(id: UUID): CompetitionDto? =
        suspendTransaction {
            Competition.findByIdAndUpdate(id) { competition ->
                competition.deleted = true
                competition.updatedAt = LocalDateTime.now()
            }?.asDto()
        }
}