package ru.pkozlov.competition.mapper

import ru.pkozlov.competition.domain.Competition
import ru.pkozlov.competition.dto.competition.CompetitionDto

fun Competition.asDto(): CompetitionDto =
    CompetitionDto(
        id = id.value,
        title = title,
        startDate = startDate,
        endDate = endDate,
        address = address,
        imagePath = imagePath,
        categories = categories,
        deleted = deleted
    )