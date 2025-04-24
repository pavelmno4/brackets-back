package ru.pkozlov.brackets.participant.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

object ParticipantTable : UUIDTable("participant") {
    val firstName: Column<String> = varchar("first_name", 255)
    val lastName: Column<String> = varchar("last_name", 255)
    val middleName: Column<String> = varchar("middle_name", 255)
    val birthDate: Column<LocalDate> = date("birth_date")
    val gender: Column<Gender> = enumerationByName<Gender>("gender", 6)
        .index("participant_gender_idx")
    val ageCategory: Column<AgeCategory> = varchar("age_category", 255)
        .transform({ AgeCategory(it) }, { it.value })
        .index("participant_age_category_idx")
    val weightCategory: Column<WeightCategory> = varchar("weight_category", 255)
        .transform({ WeightCategory(it) }, { it.value })
        .index("participant_weight_category_idx")
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

    var firstName: String by ParticipantTable.firstName
    var lastName: String by ParticipantTable.lastName
    var middleName: String by ParticipantTable.middleName
    var birthDate: LocalDate by ParticipantTable.birthDate
    var gender: Gender by ParticipantTable.gender
    var ageCategory: AgeCategory by ParticipantTable.ageCategory
    var weightCategory: WeightCategory by ParticipantTable.weightCategory
    var weight: BigDecimal? by ParticipantTable.weight
    var team: Team by Team referencedOn ParticipantTable.teamId
    var competitionId: UUID by ParticipantTable.competitionId
}