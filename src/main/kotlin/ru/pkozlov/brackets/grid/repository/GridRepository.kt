package ru.pkozlov.brackets.grid.repository

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.participant.dto.criteria.Criteria
import java.util.*

interface GridRepository {
    suspend fun findBy(
        competitionId: UUID,
        gender: Gender?,
        ageCategory: AgeCategory?,
        weightCategory: WeightCategory?
    ): List<Grid>

    suspend fun create(init: Grid.() -> Unit): Grid

    suspend fun update(id: UUID, action: (it: Grid) -> Unit): Grid?

    suspend fun deleteByCriteria(
        competitionId: UUID,
        criteria: Collection<Criteria<*>>,
    ): Int
}