package ru.pkozlov.brackets.competition.mapper

import ru.pkozlov.brackets.competition.domain.Competition
import ru.pkozlov.brackets.competition.dto.CompetitionDto

fun Competition.asDto(): CompetitionDto =
    CompetitionDto(
        id = id.value,
        title = title,
        startDate = startDate,
        endDate = endDate,
        address = address,
        imagePath = imagePath,
        categories = categories,
        stage = stage,
        deleted = deleted
    )