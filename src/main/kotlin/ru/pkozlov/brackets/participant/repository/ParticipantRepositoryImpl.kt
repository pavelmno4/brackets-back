package ru.pkozlov.brackets.participant.repository

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.domain.ParticipantTable
import ru.pkozlov.brackets.participant.domain.TeamTable
import ru.pkozlov.brackets.participant.dto.criteria.*
import java.util.*

class ParticipantRepositoryImpl : ParticipantRepository {
    override suspend fun findAllByCompetitionId(competitionId: UUID): List<Participant> =
        Participant
            .find { ParticipantTable.competitionId eq competitionId }
            .with(Participant::team)
            .toList()

    override suspend fun findAllByCriteria(
        competitionId: UUID,
        criteria: Collection<Criteria<*>>
    ): List<Participant> {
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

        return Participant.wrapRows(query).with(Participant::team).toList()
    }

    override suspend fun findById(id: UUID): Participant? =
        Participant
            .findById(id)
            ?.load(Participant::team)

    override suspend fun create(init: Participant.() -> Unit): Participant =
        Participant.new(init)

    override suspend fun update(id: UUID, action: (Participant) -> Unit): Participant? =
        Participant.findByIdAndUpdate(id, action)?.load(Participant::team)

    override suspend fun delete(id: UUID): Unit? =
        Participant.findById(id)?.delete()

    override suspend fun deleteAllWhereWeightIsNull(competitionId: UUID): Int =
        ParticipantTable.deleteWhere {
            ParticipantTable.competitionId eq competitionId and
                    (ParticipantTable.weight.isNull())
        }
}