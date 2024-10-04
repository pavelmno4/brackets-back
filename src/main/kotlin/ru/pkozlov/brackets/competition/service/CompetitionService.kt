package ru.pkozlov.brackets.competition.service

import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
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

    suspend fun findUpcoming(): List<CompetitionDto> =
        competitionRepository.findUpcoming()
            .sortedByDescending { competition -> competition.endDate }

    suspend fun findPast(): List<CompetitionDto> =
        competitionRepository.findPast()
            .sortedByDescending { competition -> competition.endDate }

    suspend fun findById(id: UUID): CompetitionDto? =
        competitionRepository.findById(id)
}