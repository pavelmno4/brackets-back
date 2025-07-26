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
                firstName = participant.firstName.trim()
                lastName = participant.lastName.trim()
                middleName = participant.middleName.trim()
                birthDate = participant.birthDate
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                rank = participant.rank
                settlement = participant.settlement
                coachFullName = participant.coachFullName
                team = newOrExistingTeam
                this.competitionId = competitionId
            }.asDto()
        }


    suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto? =
        suspendTransaction {
            val newOrExistingTeam =
                updatedParticipant.team?.run { teamComponent.findOrCreateTeam(updatedParticipant.team) }
            participantRepository.update(id) { participant ->
                if (updatedParticipant.firstName != null) participant.firstName = updatedParticipant.firstName.trim()
                if (updatedParticipant.lastName != null) participant.lastName = updatedParticipant.lastName.trim()
                if (updatedParticipant.middleName != null) participant.middleName = updatedParticipant.middleName.trim()
                if (updatedParticipant.birthDate != null) participant.birthDate = updatedParticipant.birthDate
                if (updatedParticipant.gender != null) participant.gender = updatedParticipant.gender
                if (updatedParticipant.ageCategory != null) participant.ageCategory = updatedParticipant.ageCategory
                if (updatedParticipant.weightCategory != null) participant.weightCategory = updatedParticipant.weightCategory
                if (updatedParticipant.weight != null) participant.weight = updatedParticipant.weight
                if (updatedParticipant.rank != null) participant.rank = updatedParticipant.rank
                if (updatedParticipant.settlement != null) participant.settlement = updatedParticipant.settlement
                if (updatedParticipant.coachFullName != null) participant.coachFullName = updatedParticipant.coachFullName
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
                .sortedBy { participant -> participant.lastName }
                .map(Participant::asDto)
        }
}