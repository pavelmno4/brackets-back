package ru.pkozlov.brackets.participant.repository

import ru.pkozlov.brackets.participant.domain.Team

interface TeamRepository {
    suspend fun findOrCreate(teamName: String): Team
}