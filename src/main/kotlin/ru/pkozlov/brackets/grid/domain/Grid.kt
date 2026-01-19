package ru.pkozlov.brackets.grid.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.json.jsonb
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.json
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import ru.pkozlov.brackets.grid.dto.Node
import ru.pkozlov.brackets.participant.domain.ParticipantTable
import java.util.*

object GridTable : UUIDTable("grid") {
    val gender: Column<Gender> = enumerationByName<Gender>("gender", 6)
        .index("grid_gender_idx")
    val ageCategory: Column<AgeCategory> = varchar("age_category", 255)
        .transform({ AgeCategory(it) }, { it.value })
        .index("grid_age_category_idx")
    val weightCategory: Column<WeightCategory> = varchar("weight_category", 255)
        .transform({ WeightCategory(it) }, { it.value })
        .index("grid_weight_category_idx")
    val dendrogram: Column<List<Node>> = jsonb<List<Node>>("dendrogram", json)
    val competitionId: Column<UUID> = uuid("competition_id")
        .references(
            ref = CompetitionTable.id,
            fkName = "grid_competition_id_fk"
        )
        .index("grid_competition_id_idx")
    val firstPlaceParticipantId: Column<UUID?> = uuid("first_place_participant_id")
        .references(
            ref = ParticipantTable.id,
            fkName = "grid_first_place_participant_id_fk"
        )
        .nullable()
    val secondPlaceParticipantId: Column<UUID?> = uuid("second_place_participant_id")
        .references(
            ref = ParticipantTable.id,
            fkName = "grid_second_place_participant_id_fk"
        )
        .nullable()
    val thirdPlaceParticipantId: Column<UUID?> = uuid("third_place_participant_id")
        .references(
            ref = ParticipantTable.id,
            fkName = "grid_third_place_participant_id_fk"
        )
        .nullable()
    val show: Column<Boolean> = bool("show")
}

class Grid(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Grid>(GridTable)

    var gender: Gender by GridTable.gender
    var ageCategory: AgeCategory by GridTable.ageCategory
    var weightCategory: WeightCategory by GridTable.weightCategory
    var dendrogram: List<Node> by GridTable.dendrogram
    var competitionId: UUID by GridTable.competitionId
    var firstPlaceParticipantId: UUID? by GridTable.firstPlaceParticipantId
    var secondPlaceParticipantId: UUID? by GridTable.secondPlaceParticipantId
    var thirdPlaceParticipantId: UUID? by GridTable.thirdPlaceParticipantId
    var show: Boolean by GridTable.show
}