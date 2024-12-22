package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.domain.Team
import ru.pkozlov.brackets.participant.domain.TeamTable

class TeamRepositoryImpl : TeamRepository {
    override suspend fun findOrCreate(teamName: String): Team =
        Team.find { TeamTable.name eq teamName }.singleOrNull()
            ?: Team.new {
                name = teamName
            }
}