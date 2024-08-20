package ru.pkozlov.participant.mapper

import ru.pkozlov.participant.domain.Participant
import ru.pkozlov.participant.dto.ParticipantDto

fun Participant.asDto(): ParticipantDto = ParticipantDto(
    name = name,
    birthYear = birthYear,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    competitionId = competitionId
)