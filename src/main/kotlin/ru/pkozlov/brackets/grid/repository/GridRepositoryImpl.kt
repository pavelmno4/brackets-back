package ru.pkozlov.brackets.grid.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.domain.GridTable
import java.util.*

class GridRepositoryImpl : GridRepository {
    override suspend fun findBy(
        competitionId: UUID,
        gender: Gender?,
        ageCategory: AgeCategory?,
        weightCategory: WeightCategory?
    ): List<Grid> {
        val query: Query = GridTable.selectAll().apply {
            andWhere { GridTable.competitionId eq competitionId }

            gender?.run { andWhere { GridTable.gender eq gender } }
            ageCategory?.run { andWhere { GridTable.ageCategory eq ageCategory } }
            weightCategory?.run { andWhere { GridTable.weightCategory eq weightCategory } }
        }
        return Grid.wrapRows(query).toList()
    }

    override suspend fun create(
        init: Grid.() -> Unit
    ): Grid =
        Grid.new(init)

    override suspend fun update(
        id: UUID,
        action: (it: Grid) -> Unit
    ): Grid? =
        Grid.findByIdAndUpdate(id, action)

    override suspend fun deleteByGenderAgeAndWeightCategory(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): Int =
        GridTable.deleteWhere {
            GridTable.competitionId eq competitionId and
                    (GridTable.gender eq gender) and
                    (GridTable.ageCategory eq ageCategory) and
                    (GridTable.weightCategory eq weightCategory)
        }

    override suspend fun deleteAllWith(
        competitionId: UUID
    ): Int =
        GridTable.deleteWhere { GridTable.competitionId eq competitionId }
}