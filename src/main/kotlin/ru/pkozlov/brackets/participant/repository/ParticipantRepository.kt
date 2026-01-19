package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import java.util.*

interface ParticipantRepository {
    suspend fun findAllByCompetitionId(competitionId: UUID): List<Participant>
    suspend fun findAllByCriteria(competitionId: UUID, criteria: Collection<Criteria<*>>): List<Participant>
    suspend fun findById(id: UUID): Participant?
    suspend fun create(init: Participant.() -> Unit): Participant
    suspend fun update(id: UUID, action: (it: Participant) -> Unit): Participant?
    suspend fun delete(id: UUID): Unit?
}