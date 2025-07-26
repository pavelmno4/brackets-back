package ru.pkozlov.brackets.participant.mapper

import ru.pkozlov.brackets.participant.domain.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto

fun Participant.asDto(): ParticipantDto = ParticipantDto(
    id = id.value,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    birthDate = birthDate,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    weight = weight,
    rank = rank,
    settlement = settlement,
    coachFullName = coachFullName,
    team = team.name
)