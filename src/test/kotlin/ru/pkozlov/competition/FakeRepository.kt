package ru.pkozlov.competition

import ru.pkozlov.competition.dto.category.Category
import ru.pkozlov.competition.dto.competition.CompetitionDto
import ru.pkozlov.competition.dto.competition.PersistCompetitionDto
import ru.pkozlov.competition.repository.CompetitionRepository
import java.time.LocalDate
import java.util.*

class FakeRepository : CompetitionRepository {
    private val repository: MutableMap<UUID, CompetitionDto> = mutableMapOf(
        UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb322") to CompetitionDto(
            id = UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb322"),
            title = "Турнир номер 1",
            startDate = LocalDate.parse("2024-09-17"),
            endDate = LocalDate.parse("2024-09-17"),
            address = "Московская обл., п. Большевик",
            imagePath = "/image",
            categories = listOf(
                Category(
                    yearRange = "2012-2011",
                    weights = setOf("52", "54")
                )
            ),
            deleted = false
        ),
        UUID.fromString("eff81db1-e3f5-47eb-bdd8-827ed5dfb322") to CompetitionDto(
            id = UUID.fromString("eff81db1-e3f5-47eb-bdd8-827ed5dfb322"),
            title = "Турнир номер 2",
            startDate = LocalDate.parse("2024-09-24"),
            endDate = LocalDate.parse("2024-09-24"),
            address = "Московская обл., п. Большевик",
            imagePath = "/image",
            categories = listOf(
                Category(
                    yearRange = "2012-2011",
                    weights = setOf("52", "54")
                )
            ),
            deleted = true
        )
    )

    override suspend fun findAll(): List<CompetitionDto> =
        repository.values.toList()

    override suspend fun findById(id: UUID): CompetitionDto? =
        repository[id]

    override suspend fun create(competition: PersistCompetitionDto): CompetitionDto {
        val id: UUID = UUID.randomUUID()
        val createdCompetition = CompetitionDto(
            id = id,
            title = competition.title,
            startDate = competition.startDate,
            endDate = competition.endDate,
            address = competition.address,
            imagePath = competition.imagePath,
            categories = competition.categories,
            deleted = false
        )

        return repository.getOrPut(id, { createdCompetition })
    }

    override suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto? {
        val competition = repository[id]?.copy(
            id = id,
            title = updatedCompetition.title,
            startDate = updatedCompetition.startDate,
            endDate = updatedCompetition.endDate,
            address = updatedCompetition.address,
            imagePath = updatedCompetition.imagePath,
            categories = updatedCompetition.categories,
            deleted = false
        )

        competition?.run { repository[id] = this }
        return repository[id]
    }

    override suspend fun delete(id: UUID): CompetitionDto? {
        return repository[id]
            ?.copy(deleted = true)
            ?.also { deletedCompetition -> repository[id] = deletedCompetition }
    }
}