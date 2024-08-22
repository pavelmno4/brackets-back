package ru.pkozlov.brackets.participant.service

import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PersistParticipantDto
import ru.pkozlov.brackets.participant.repository.ParticipantRepository
import java.util.*

class ParticipantService(
    private val participantRepository: ParticipantRepository
) {
    suspend fun create(competitionId: UUID, participant: PersistParticipantDto): ParticipantDto =
        participantRepository.create(competitionId, participant)

    suspend fun update(id: UUID, updatedParticipant: PersistParticipantDto): ParticipantDto? =
        participantRepository.update(id, updatedParticipant)

    suspend fun delete(id: UUID): Unit? =
        participantRepository.delete(id)
    
    suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto> =
        participantRepository.findAllByCompetitionId(competitionId)
}