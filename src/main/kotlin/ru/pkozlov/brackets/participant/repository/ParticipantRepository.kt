package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import java.util.*

interface ParticipantRepository {
    suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto>
    suspend fun findById(id: UUID): ParticipantDto?
    suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto
    suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto?
    suspend fun delete(id: UUID): Unit?
}