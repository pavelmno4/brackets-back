package ru.pkozlov.participant.repository

import ru.pkozlov.participant.domain.Participant
import ru.pkozlov.participant.domain.ParticipantTable
import ru.pkozlov.participant.dto.ParticipantDto
import ru.pkozlov.participant.dto.PersistParticipantDto
import ru.pkozlov.participant.mapper.asDto
import ru.pkozlov.utils.suspendTransaction
import java.util.*

class ParticipantRepositoryImpl : ParticipantRepository {
    override suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto> =
        suspendTransaction {
            Participant
                .find { ParticipantTable.competitionId eq competitionId }
                .map(Participant::asDto)
        }

    override suspend fun findById(id: UUID): ParticipantDto? =
        suspendTransaction {
            Participant.findById(id)?.asDto()
        }

    override suspend fun create(competitionId: UUID, participant: PersistParticipantDto): ParticipantDto =
        suspendTransaction {
            Participant.new {
                name = participant.name
                birthYear = participant.birthYear
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                this.competitionId = competitionId
            }.asDto()
        }

    override suspend fun update(id: UUID, updatedParticipant: PersistParticipantDto): ParticipantDto? =
        suspendTransaction {
            Participant.findByIdAndUpdate(id) { participant ->
                participant.name = updatedParticipant.name
                participant.birthYear = updatedParticipant.birthYear
                participant.gender = updatedParticipant.gender
                participant.ageCategory = updatedParticipant.ageCategory
                participant.weightCategory = updatedParticipant.weightCategory
            }?.asDto()
        }

    override suspend fun delete(id: UUID): Unit? =
        suspendTransaction {
            Participant.findById(id)?.delete()
        }
}