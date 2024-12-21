package ru.pkozlov.brackets.participant.service

import ru.pkozlov.brackets.participant.domain.Team
import ru.pkozlov.brackets.participant.repository.TeamRepository

class TeamComponent(
    private val teamRepository: TeamRepository
) {
    suspend fun findOrCreateTeam(teamName: String): Team = teamRepository.findOrCreate(teamName)
}