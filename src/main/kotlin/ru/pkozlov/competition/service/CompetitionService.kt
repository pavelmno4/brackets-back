package ru.pkozlov.competition.service

import ru.pkozlov.competition.dto.competition.CompetitionDto
import ru.pkozlov.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.competition.repository.CompetitionRepository
import java.util.*

class CompetitionService(
    private val competitionRepository: CompetitionRepository
) {
    suspend fun create(competition: PersistCompetitionDto): CompetitionDto =
        competitionRepository.create(competition)

    suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto? =
        competitionRepository.update(id, updatedCompetition)

    suspend fun delete(id: UUID): CompetitionDto? =
        competitionRepository.delete(id)

    suspend fun findAll(): List<CompetitionDto> =
        competitionRepository.findAll()

    suspend fun findById(id: UUID): CompetitionDto? =
        competitionRepository.findById(id)
}