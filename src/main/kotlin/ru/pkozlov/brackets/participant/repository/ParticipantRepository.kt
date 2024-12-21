package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import java.util.*

interface ParticipantRepository {
    suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto>
    suspend fun findAllByCriteria(competitionId: UUID, criteria: Collection<Criteria<*>>): List<ParticipantDto>
    suspend fun findById(id: UUID): ParticipantDto?
    suspend fun create(init: Participant.() -> Unit): ParticipantDto
    suspend fun update(id: UUID, action: (it: Participant) -> Unit): ParticipantDto?
    suspend fun delete(id: UUID): Unit?
}