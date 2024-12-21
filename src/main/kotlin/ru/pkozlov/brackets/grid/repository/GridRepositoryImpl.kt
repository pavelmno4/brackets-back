package ru.pkozlov.brackets.grid.repository

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.domain.GridTable
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
                .singleOrNull()
                ?.asDto()
        }

    override suspend fun create(
        init: Grid.() -> Unit
    ): GridDto =
        suspendTransaction {
            Grid.new(init).asDto()
        }

    override suspend fun update(
        id: UUID,
        action: (it: Grid) -> Unit
    ): GridDto? =
        suspendTransaction {
            Grid.findByIdAndUpdate(id, action)?.asDto()
        }

    override suspend fun deleteAllWith(
        competitionId: UUID
    ): Int =
        suspendTransaction {
            GridTable.deleteWhere { GridTable.competitionId eq competitionId }
        }
}