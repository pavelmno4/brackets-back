package ru.pkozlov.brackets.participant.service

import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import ru.pkozlov.brackets.participant.repository.ParticipantRepository
import java.util.*

class ParticipantService(
    private val participantRepository: ParticipantRepository
) {
    suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto =
        try {
            participantRepository.create(competitionId, participant)
        } catch (exc: ExposedSQLException) {
            throw IllegalArgumentException(exc)
        }

    suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto? =
        try {
            participantRepository.update(id, updatedParticipant)
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