package ru.pkozlov.brackets.grid.repository

import org.jetbrains.exposed.sql.and
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.domain.GridTable
import ru.pkozlov.brackets.grid.dto.CreateGridDto
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.mapper.asDto
import java.util.*

class GridRepositoryImpl : GridRepository {
    override suspend fun findBy(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): GridDto? =
        suspendTransaction {
            Grid
                .find {
                    GridTable.competitionId eq competitionId and
                            (GridTable.gender eq gender) and
                            (GridTable.ageCategory eq ageCategory) and
                            (GridTable.weightCategory eq weightCategory)
                }
                .map(Grid::asDto)
                .singleOrNull()
        }

    override suspend fun create(
        grid: CreateGridDto
    ): GridDto =
        suspendTransaction {
            Grid.new {
                competitionId = grid.competitionId
                gender = grid.gender
                ageCategory = grid.ageCategory
                weightCategory = grid.weightCategory
                dendrogram = grid.dendrogram
            }.asDto()
        }
}