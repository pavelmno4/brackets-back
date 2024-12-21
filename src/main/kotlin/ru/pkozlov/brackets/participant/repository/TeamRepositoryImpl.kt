package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.participant.domain.Team
import ru.pkozlov.brackets.participant.domain.TeamTable

class TeamRepositoryImpl : TeamRepository {
    override suspend fun findOrCreate(teamName: String): Team =
        suspendTransaction {
            Team.find { TeamTable.name eq teamName }.singleOrNull()
                ?: Team.new {
                    name = teamName
                }
        }
}