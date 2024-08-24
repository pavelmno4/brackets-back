package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.domain.ParticipantTable
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.mapper.asDto
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
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

    override suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto =
        suspendTransaction {
            Participant.new {
                fullName = participant.fullName
                birthYear = participant.birthYear
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                this.competitionId = competitionId
            }.asDto()
        }

    override suspend fun update(id: UUID, updatedParticipant: PatchParticipantDto): ParticipantDto? =
        suspendTransaction {
            Participant.findByIdAndUpdate(id) { participant ->
                if (updatedParticipant.fullName != null) participant.fullName = updatedParticipant.fullName
                if (updatedParticipant.birthYear != null) participant.birthYear = updatedParticipant.birthYear
                if (updatedParticipant.gender != null) participant.gender = updatedParticipant.gender
                if (updatedParticipant.ageCategory != null) participant.ageCategory = updatedParticipant.ageCategory
                if (updatedParticipant.weightCategory != null) participant.weightCategory = updatedParticipant.weightCategory
                if (updatedParticipant.weight != null) participant.weight = updatedParticipant.weight
            }?.asDto()
        }

    override suspend fun delete(id: UUID): Unit? =
        suspendTransaction {
            Participant.findById(id)?.delete()
        }
}