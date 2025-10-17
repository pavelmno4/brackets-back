package ru.pkozlov.brackets.viewer.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import ru.pkozlov.brackets.competition.domain.CompetitionTable
import java.util.*

object ViewerTable : UUIDTable("viewer") {
    val firstName: Column<String> = varchar("first_name", 255)
    val lastName: Column<String> = varchar("last_name", 255)
    val middleName: Column<String> = varchar("middle_name", 255)
    val phone: Column<String> = varchar("phone", 255)
    val competitionId: Column<UUID> = uuid("competition_id")
        .references(
            ref = CompetitionTable.id,
            fkName = "viewer_competition_id_fk"
        )
        .index("viewer_competition_id_idx")
}

class Viewer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Viewer>(ViewerTable)

    var firstName: String by ViewerTable.firstName
    var lastName: String by ViewerTable.lastName
    var middleName: String by ViewerTable.middleName
    var phone: String by ViewerTable.phone
    var competitionId: UUID by ViewerTable.competitionId
}