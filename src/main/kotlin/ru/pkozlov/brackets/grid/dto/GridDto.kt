package ru.pkozlov.brackets.grid.dto

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory

data class GridDto(
    val tournamentName: String,
    val ageCategory: AgeCategory,
    val weightCategory: WeightCategory,
    val templatePath: String,
    val graph: Node
)