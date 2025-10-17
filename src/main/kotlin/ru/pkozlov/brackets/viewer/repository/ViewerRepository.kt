package ru.pkozlov.brackets.viewer.repository

import ru.pkozlov.brackets.viewer.domain.Viewer
import java.util.*

interface ViewerRepository {
    suspend fun findAllByCompetitionId(competitionId: UUID): List<Viewer>
    suspend fun create(init: Viewer.() -> Unit): Viewer
}