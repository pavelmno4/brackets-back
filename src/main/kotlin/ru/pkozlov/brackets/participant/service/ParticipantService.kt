package ru.pkozlov.brackets.participant.service

import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import ru.pkozlov.brackets.participant.repository.ParticipantRepository
import java.util.*

class ParticipantService(
    private val participantRepository: ParticipantRepository,
    private val teamComponent: TeamComponent
) {
    suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto =
        try {
            val newOrExistingTeam = teamComponent.findOrCreateTeam(participant.team)
            participantRepository.create {
                fullName = participant.fullName
                birthDate = participant.birthDate
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                team = newOrExistingTeam
                this.competitionId = competitionId
            }
        } catch (exc: ExposedSQLException) {
            throw IllegalArgumentException(exc)
        }

    suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto? =
        try {
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
            }
        } catch (exc: ExposedSQLException) {
            throw IllegalArgumentException(exc)
        }

    suspend fun delete(id: UUID): Unit? =
        try {
            participantRepository.delete(id)
        } catch (exc: ExposedSQLException) {
            throw IllegalArgumentException(exc)
        }

    suspend fun findAllByCriteria(competitionId: UUID, criteria: Collection<Criteria<*>>): List<ParticipantDto> =
        try {
            participantRepository.findAllByCriteria(competitionId, criteria)
                .sortedBy { participant -> participant.fullName }
        } catch (exc: ExposedSQLException) {
            throw IllegalArgumentException(exc)
        }
}