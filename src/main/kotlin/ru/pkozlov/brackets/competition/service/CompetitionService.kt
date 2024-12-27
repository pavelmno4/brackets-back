package ru.pkozlov.brackets.competition.service

import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.brackets.competition.enumeration.Stage.*
import ru.pkozlov.brackets.competition.mapper.asDto
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
import ru.pkozlov.brackets.grid.service.GridService
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.time.LocalDateTime
import java.util.*

class CompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val participantService: ParticipantService,
    private val gridService: GridService,
    private val now: () -> LocalDateTime
) {
    suspend fun create(competition: PersistCompetitionDto): CompetitionDto = suspendTransaction {
        competitionRepository.create {
            title = competition.title
            startDate = competition.startDate
            endDate = competition.endDate
            address = competition.address
            imagePath = competition.imagePath
            categories = competition.categories
            stage = REGISTRATION
            deleted = false
            createdAt = now()
            updatedAt = now()
        }.asDto()
    }

    suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto? = suspendTransaction {
        competitionRepository.update(id) { competition ->
            competition.title = updatedCompetition.title
            competition.startDate = updatedCompetition.startDate
            competition.endDate = updatedCompetition.endDate
            competition.address = updatedCompetition.address
            competition.imagePath = updatedCompetition.imagePath
            competition.categories = updatedCompetition.categories
            competition.updatedAt = now()
        }?.asDto()
    }

    suspend fun delete(id: UUID): CompetitionDto? = suspendTransaction {
        competitionRepository.update(id) { competition ->
            competition.deleted = true
            competition.updatedAt = now()
        }?.asDto()
    }

    suspend fun startCompetition(id: UUID): CompetitionDto? =
        suspendTransaction {
            competitionRepository.update(id) { competition ->
                competition.stage = RUNNING
                competition.updatedAt = now()
            }?.asDto()
        }?.also {
            participantService.deleteAllWhereWeightIsNull(id)
            gridService.generateAutomatically(id)
        }

    suspend fun completeCompetition(id: UUID): CompetitionDto? = suspendTransaction {
        competitionRepository.update(id) { competition ->
            competition.stage = COMPLETED
            competition.updatedAt = now()
        }?.asDto()
    }

    suspend fun findUpcoming(): List<CompetitionDto> = suspendTransaction {
        competitionRepository.findWhereEndDateGreaterOrEq(now().toLocalDate())
            .sortedByDescending { competition -> competition.endDate }
            .map(Competition::asDto)
    }

    suspend fun findPast(): List<CompetitionDto> = suspendTransaction {
        competitionRepository.findWhereEndDateLess(now().toLocalDate())
            .sortedByDescending { competition -> competition.endDate }
            .map(Competition::asDto)
    }

    suspend fun findById(id: UUID): CompetitionDto? = suspendTransaction {
        competitionRepository.findById(id)?.asDto()
    }

    suspend fun isPassed(id: UUID): Boolean = suspendTransaction {
        competitionRepository.findById(id)
            ?.run { endDate < now().toLocalDate() }
            ?: true
    }
}