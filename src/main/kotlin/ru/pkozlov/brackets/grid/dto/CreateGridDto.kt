package ru.pkozlov.brackets.grid.dto

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import java.util.*

data class CreateGridDto(
    val competitionId: UUID,
    val gender: Gender,
    val ageCategory: AgeCategory,
    val weightCategory: WeightCategory,
    val hierarchy: Node
)