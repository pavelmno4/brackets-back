package ru.pkozlov.brackets.competition.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.json.jsonb
import ru.pkozlov.brackets.app.utils.json
import ru.pkozlov.brackets.competition.dto.category.CategoriesByGender
import ru.pkozlov.brackets.competition.enumeration.Stage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

object CompetitionTable : UUIDTable("competition") {
    val title: Column<String> = varchar("title", 255)
    val startDate: Column<LocalDate> = date("start_date")
    val endDate: Column<LocalDate> = date("end_sate")
    val address: Column<String> = varchar("address", 255)
    val imagePath: Column<String> = varchar("image_path", 255)
    val categories: Column<CategoriesByGender> = jsonb("categories", json)
    val chiefSecretary: Column<String?> = varchar("chief_secretary", 255).nullable()
    val chiefJudge: Column<String?> = varchar("chief_judge", 255).nullable()
    val stage: Column<Stage> = enumerationByName<Stage>("stage", 30)
    val deleted: Column<Boolean> = bool("deleted")
    val createdAt: Column<LocalDateTime> = datetime("created_at")
    val updatedAt: Column<LocalDateTime> = datetime("updated_at")
}

class Competition(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Competition>(CompetitionTable)

    var title: String by CompetitionTable.title
    var startDate: LocalDate by CompetitionTable.startDate
    var endDate: LocalDate by CompetitionTable.endDate
    var address: String by CompetitionTable.address
    var imagePath: String by CompetitionTable.imagePath
    var categories: CategoriesByGender by CompetitionTable.categories
    var chiefSecretary: String? by CompetitionTable.chiefSecretary
    var chiefJudge: String? by CompetitionTable.chiefJudge
    var stage: Stage by CompetitionTable.stage
    var deleted: Boolean by CompetitionTable.deleted
    var createdAt: LocalDateTime by CompetitionTable.createdAt
    var updatedAt: LocalDateTime by CompetitionTable.updatedAt
}
