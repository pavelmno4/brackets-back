package ru.pkozlov.brackets.grid.repository

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.dto.CreateGridDto
import ru.pkozlov.brackets.grid.dto.GridDto
import java.util.*

interface GridRepository {
    suspend fun findBy(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): GridDto?

    suspend fun create(grid: CreateGridDto): GridDto
    suspend fun update(id: UUID, action: (it: Grid) -> Unit): GridDto?
    suspend fun deleteAllWith(competitionId: UUID): Int
}