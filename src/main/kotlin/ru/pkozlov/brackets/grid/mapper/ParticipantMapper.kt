package ru.pkozlov.brackets.grid.mapper

import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.grid.dto.ParticipantDto as GridParticipantDto

fun ParticipantDto.asGridParticipant(): GridParticipantDto =
    GridParticipantDto(
        fullName = fullName,
        team = team
    )