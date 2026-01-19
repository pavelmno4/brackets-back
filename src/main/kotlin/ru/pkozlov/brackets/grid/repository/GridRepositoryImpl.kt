package ru.pkozlov.brackets.grid.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.domain.GridTable
import ru.pkozlov.brackets.participant.dto.criteria.AgeCategoryCriteria
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import ru.pkozlov.brackets.participant.dto.criteria.GenderCriteria
import ru.pkozlov.brackets.participant.dto.criteria.WeightCategoryCriteria
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

    override suspend fun deleteByCriteria(
        competitionId: UUID,
        criteria: Collection<Criteria<*>>,
    ): Int =
        GridTable.deleteWhere {
            criteria.fold(GridTable.competitionId eq competitionId) { condition, criteriaElement ->
                when (criteriaElement) {
                    is GenderCriteria -> condition and (GridTable.gender eq criteriaElement.value)
                    is AgeCategoryCriteria -> condition and (GridTable.ageCategory eq criteriaElement.value)
                    is WeightCategoryCriteria -> condition and (GridTable.weightCategory eq criteriaElement.value)
                    else -> condition
                }
            }
        }
}