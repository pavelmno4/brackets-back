package ru.pkozlov.brackets.competition.repository

import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.dto.competition.PersistCompetitionDto
import java.util.*

interface CompetitionRepository {
    suspend fun findAll(): List<CompetitionDto>
    suspend fun findById(id: UUID): CompetitionDto?
    suspend fun create(competition: PersistCompetitionDto): CompetitionDto
    suspend fun update(id: UUID, updatedCompetition: PersistCompetitionDto): CompetitionDto?
    suspend fun delete(id: UUID): CompetitionDto?
}