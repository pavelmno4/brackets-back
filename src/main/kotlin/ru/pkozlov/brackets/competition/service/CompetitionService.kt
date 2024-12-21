package ru.pkozlov.brackets.competition.service

import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
import java.time.LocalDateTime
import java.util.*

class CompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val now: () -> LocalDateTime
) {
    suspend fun create(competition: PersistCompetitionDto): CompetitionDto =
        competitionRepository.create {
            title = competition.title
            startDate = competition.startDate
            endDate = competition.endDate
            address = competition.address
            imagePath = competition.imagePath
            categories = competition.categories
            deleted = false
            createdAt = now()
            updatedAt = now()
        }

    suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto? =
        competitionRepository.update(id) { competition ->
            competition.title = updatedCompetition.title
            competition.startDate = updatedCompetition.startDate
            competition.endDate = updatedCompetition.endDate
            competition.address = updatedCompetition.address
            competition.imagePath = updatedCompetition.imagePath
            competition.categories = updatedCompetition.categories
            competition.updatedAt = now()
        }

    suspend fun delete(id: UUID): CompetitionDto? =
        competitionRepository.update(id) { competition ->
            competition.deleted = true
            competition.updatedAt = now()
        }

    suspend fun findUpcoming(): List<CompetitionDto> =
        competitionRepository.findWhereEndDateGreaterOrEq(now().toLocalDate())
            .sortedByDescending { competition -> competition.endDate }

    suspend fun findPast(): List<CompetitionDto> =
        competitionRepository.findWhereEndDateLess(now().toLocalDate())
            .sortedByDescending { competition -> competition.endDate }

    suspend fun findById(id: UUID): CompetitionDto? =
        competitionRepository.findById(id)

    suspend fun isPassed(id: UUID): Boolean =
        competitionRepository.findById(id)
            ?.run { endDate < now().toLocalDate() }
            ?: true
}