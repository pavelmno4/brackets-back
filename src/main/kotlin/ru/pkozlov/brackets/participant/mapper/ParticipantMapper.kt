package ru.pkozlov.brackets.participant.mapper

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto

fun Participant.asDto(): ParticipantDto = ParticipantDto(
    id = id.value,
    fullName = fullName,
    birthDate = birthDate,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    weight = weight,
    team = team.name
)