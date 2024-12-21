package ru.pkozlov.brackets.competition

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.dto.category.CategoriesByGender
import ru.pkozlov.brackets.competition.dto.category.Category
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.repository.CompetitionRepository
import java.time.LocalDate
import java.util.*

// ToDo: move to testcontainers
class FakeCompetitionRepository : CompetitionRepository {
    private val repository: MutableMap<UUID, CompetitionDto> = mutableMapOf(
        UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb322") to CompetitionDto(
            id = UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb322"),
            title = "Турнир номер 1",
            startDate = LocalDate.parse("2024-09-17"),
            endDate = LocalDate.parse("2024-09-17"),
            address = "Московская обл., п. Большевик",
            imagePath = "/image",
            categories = CategoriesByGender(
                male = listOf(
                    Category(
                        yearRange = AgeCategory("2012-2011"),
                        weights = setOf(WeightCategory("52"), WeightCategory("54"))
                    )
                ),
                female = emptyList()
            ),
            deleted = false
        ),
        UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb323") to CompetitionDto(
            id = UUID.fromString("eff81db2-e3f5-47eb-bdd8-827ed5dfb322"),
            title = "Турнир номер 2",
            startDate = LocalDate.parse("2024-09-05"),
            endDate = LocalDate.parse("2024-09-05"),
            address = "Московская обл., п. Большевик",
            imagePath = "/image",
            categories = CategoriesByGender(
                male = listOf(
                    Category(
                        yearRange = AgeCategory("2012-2011"),
                        weights = setOf(WeightCategory("52"), WeightCategory("54"))
                    )
                ),
                female = emptyList()
            ),
            deleted = false
        ),
        UUID.fromString("eff81db1-e3f5-47eb-bdd8-827ed5dfb324") to CompetitionDto(
            id = UUID.fromString("eff81db1-e3f5-47eb-bdd8-827ed5dfb322"),
            title = "Турнир номер 3",
            startDate = LocalDate.parse("2024-09-24"),
            endDate = LocalDate.parse("2024-09-24"),
            address = "Московская обл., п. Большевик",
            imagePath = "/image",
            categories = CategoriesByGender(
                male = listOf(
                    Category(
                        yearRange = AgeCategory("2012-2011"),
                        weights = setOf(WeightCategory("52"), WeightCategory("54"))
                    )
                ),
                female = emptyList()
            ),
            deleted = true
        )
    )

    override suspend fun findWhereEndDateGreaterOrEq(date: LocalDate): List<CompetitionDto> =
        repository.values
            .filter { it.endDate >= date && !it.deleted }

    override suspend fun findWhereEndDateLess(date: LocalDate): List<CompetitionDto> =
        repository.values
            .filter { it.endDate < date && !it.deleted }

    override suspend fun findById(id: UUID): CompetitionDto? =
        repository[id]

    override suspend fun create(init: Competition.() -> Unit): CompetitionDto {
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

    override suspend fun update(id: UUID, action: (Competition) -> Unit): CompetitionDto? {
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
}