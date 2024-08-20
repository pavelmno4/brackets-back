package ru.pkozlov.participant.repository

import ru.pkozlov.participant.dto.ParticipantDto
import ru.pkozlov.participant.dto.PersistParticipantDto
import java.util.*

interface ParticipantRepository {
    suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto>
    suspend fun findById(id: UUID): ParticipantDto?
    suspend fun create(competitionId: UUID, participant: PersistParticipantDto): ParticipantDto
    suspend fun update(id: UUID, updatedParticipant: PersistParticipantDto): ParticipantDto?
    suspend fun delete(id: UUID): Unit?
}