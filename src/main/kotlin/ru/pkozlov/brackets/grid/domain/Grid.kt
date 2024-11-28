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
    val dendrogram: Column<Node?> = jsonb<Node>("dendrogram", json).nullable()
    val competitionId: Column<UUID> = uuid("competition_id")
        .references(
            ref = CompetitionTable.id,
            fkName = "grid_competition_id_fk"
        )
        .index("grid_competition_id_idx")
}

class Grid(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Grid>(GridTable)

    var gender: Gender by GridTable.gender
    var ageCategory: AgeCategory by GridTable.ageCategory
    var weightCategory: WeightCategory by GridTable.weightCategory
    var dendrogram: Node? by GridTable.dendrogram
    var competitionId: UUID by GridTable.competitionId
}