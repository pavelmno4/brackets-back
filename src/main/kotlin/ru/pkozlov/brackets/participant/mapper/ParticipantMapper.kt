package ru.pkozlov.brackets.participant.mapper

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto

fun Participant.asDto(): ParticipantDto = ParticipantDto(
    name = name,
    birthYear = birthYear,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    competitionId = competitionId
)