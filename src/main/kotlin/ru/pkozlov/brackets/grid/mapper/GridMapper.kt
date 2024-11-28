package ru.pkozlov.brackets.grid.mapper

import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.dto.GridDto

fun Grid.asDto(): GridDto = GridDto(
    id = id.value,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    dendrogram = dendrogram,
    competitionId = competitionId
)