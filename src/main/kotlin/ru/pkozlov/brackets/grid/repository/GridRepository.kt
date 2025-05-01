package ru.pkozlov.brackets.grid.repository

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.domain.Grid
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

    suspend fun deleteAllWith(competitionId: UUID): Int

    suspend fun deleteByGenderAgeAndWeightCategory(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): Int
}