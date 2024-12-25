package ru.pkozlov.brackets.participant.service

import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import ru.pkozlov.brackets.participant.mapper.asDto
import ru.pkozlov.brackets.participant.repository.ParticipantRepository
import java.util.*

class ParticipantService(
    private val participantRepository: ParticipantRepository,
    private val teamComponent: TeamComponent
) {
    suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto =
        suspendTransaction {
            val newOrExistingTeam = teamComponent.findOrCreateTeam(participant.team)
            participantRepository.create {
                fullName = participant.fullName
                birthDate = participant.birthDate
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                team = newOrExistingTeam
                this.competitionId = competitionId
            }.asDto()
        }


    suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto? =
        suspendTransaction {
            val newOrExistingTeam =
                updatedParticipant.team?.run { teamComponent.findOrCreateTeam(updatedParticipant.team) }
            participantRepository.update(id) { participant ->
                if (updatedParticipant.fullName != null) participant.fullName = updatedParticipant.fullName
                if (updatedParticipant.birthDate != null) participant.birthDate = updatedParticipant.birthDate
                if (updatedParticipant.gender != null) participant.gender = updatedParticipant.gender
                if (updatedParticipant.ageCategory != null) participant.ageCategory = updatedParticipant.ageCategory
                if (updatedParticipant.weightCategory != null) participant.weightCategory = updatedParticipant.weightCategory
                if (updatedParticipant.weight != null) participant.weight = updatedParticipant.weight
                if (newOrExistingTeam != null) participant.team = newOrExistingTeam
            }?.asDto()
        }


    suspend fun delete(id: UUID): Unit? =
        suspendTransaction {
            participantRepository.delete(id)
        }

    suspend fun deleteAllWhereWeightIsNull(competitionId: UUID): Int =
        suspendTransaction {
            participantRepository.deleteAllWhereWeightIsNull(competitionId)
        }

    suspend fun findAllByCriteria(competitionId: UUID, criteria: Collection<Criteria<*>>): List<ParticipantDto> =
        suspendTransaction {
            participantRepository.findAllByCriteria(competitionId, criteria)
                .sortedBy { participant -> participant.fullName }
                .map(Participant::asDto)
        }
}