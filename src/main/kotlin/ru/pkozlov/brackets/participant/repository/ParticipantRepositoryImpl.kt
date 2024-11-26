package ru.pkozlov.brackets.participant.repository

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.domain.ParticipantTable
import ru.pkozlov.brackets.participant.domain.Team
import ru.pkozlov.brackets.participant.domain.TeamTable
import ru.pkozlov.brackets.participant.dto.CreateParticipantDto
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.PatchParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.*
import ru.pkozlov.brackets.participant.mapper.asDto
import java.util.*

class ParticipantRepositoryImpl : ParticipantRepository {
    override suspend fun findAllByCompetitionId(competitionId: UUID): List<ParticipantDto> =
        suspendTransaction {
            Participant
                .find { ParticipantTable.competitionId eq competitionId }
                .with(Participant::team)
                .map(Participant::asDto)
        }

    override suspend fun findAllByCriteria(
        competitionId: UUID,
        criteria: Collection<Criteria<*>>
    ): List<ParticipantDto> =
        suspendTransaction {
            val query: Query = ParticipantTable.selectAll().apply {
                andWhere { ParticipantTable.competitionId eq competitionId }

                criteria.forEach { criteria ->
                    when (criteria) {
                        is GenderCriteria -> andWhere { ParticipantTable.gender eq criteria.value }
                        is AgeCategoryCriteria -> andWhere { ParticipantTable.ageCategory eq criteria.value }
                        is WeightCategoryCriteria -> andWhere { ParticipantTable.weightCategory eq criteria.value }
                        is TeamCriteria -> adjustColumnSet { innerJoin(TeamTable, { ParticipantTable.teamId }, { TeamTable.id }) }
                            .adjustSelect { select(fields + TeamTable.columns) }
                            .andWhere { TeamTable.name eq criteria.value }
                    }
                }
            }

            Participant.wrapRows(query).with(Participant::team).map(Participant::asDto)
        }

    override suspend fun findById(id: UUID): ParticipantDto? =
        suspendTransaction {
            Participant
                .findById(id)
                ?.load(Participant::team)
                ?.asDto()
        }

    override suspend fun create(competitionId: UUID, participant: CreateParticipantDto): ParticipantDto =
        suspendTransaction {
            Participant.new {
                fullName = participant.fullName
                birthYear = participant.birthYear
                gender = participant.gender
                ageCategory = participant.ageCategory
                weightCategory = participant.weightCategory
                team = findOrCreateTeam(participant.team)
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
                if (updatedParticipant.team != null) participant.team = findOrCreateTeam(updatedParticipant.team)
            }?.load(Participant::team)?.asDto()
        }

    override suspend fun delete(id: UUID): Unit? =
        suspendTransaction {
            Participant.findById(id)?.delete()
        }

    private fun findOrCreateTeam(teamName: String): Team =
        Team.find { TeamTable.name eq teamName }.singleOrNull()
            ?: Team.new {
                name = teamName
            }
}