package ru.pkozlov.brackets.participant.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object TeamTable : UUIDTable("team") {
    val name: Column<String> = varchar("name", 255).uniqueIndex("team_name_idx")
}

class Team(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Team>(TeamTable)

    var name: String by TeamTable.name
}