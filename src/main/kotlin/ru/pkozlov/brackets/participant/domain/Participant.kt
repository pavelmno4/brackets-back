package ru.pkozlov.brackets.participant.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.participant.enumeration.Gender
import java.math.BigDecimal
import java.util.*

object ParticipantTable : UUIDTable("participant") {
    val fullName: Column<String> = varchar("full_name", 255)
    val birthYear: Column<Int> = integer("birth_year")
    val gender: Column<Gender> = enumerationByName("gender", 6)
    val ageCategory: Column<String> = varchar("age_category", 255)
    val weightCategory: Column<String> = varchar("weight_category", 255)
    val weight: Column<BigDecimal?> = decimal("weight", 5, 2).nullable()
    val teamId: Column<EntityID<UUID>> = reference(
        name = "team_id",
        foreign = TeamTable,
        fkName = "participant_team_id_fk"
    )
        .index("participant_team_id_idx")
    val competitionId: Column<UUID> = uuid("competition_id")
        .references(
            ref = CompetitionTable.id,
            fkName = "participant_competition_id_fk"
        )
        .index("participant_competition_id_idx")
}

class Participant(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Participant>(ParticipantTable)

    var fullName: String by ParticipantTable.fullName
    var birthYear: Int by ParticipantTable.birthYear
    var gender: Gender by ParticipantTable.gender
    var ageCategory: AgeCategory by ParticipantTable.ageCategory.transform({ it.value }, { AgeCategory(it) })
    var weightCategory: WeightCategory by ParticipantTable.weightCategory.transform({ it.value }, { WeightCategory(it) })
    var weight: BigDecimal? by ParticipantTable.weight
    var team: Team by Team referencedOn ParticipantTable.teamId
    var competitionId: UUID by ParticipantTable.competitionId
}