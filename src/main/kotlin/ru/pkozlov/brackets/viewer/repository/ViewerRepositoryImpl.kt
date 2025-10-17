package ru.pkozlov.brackets.viewer.repository

import ru.pkozlov.brackets.viewer.domain.Viewer
import ru.pkozlov.brackets.viewer.domain.ViewerTable
import java.util.*

class ViewerRepositoryImpl : ViewerRepository {
    override suspend fun findAllByCompetitionId(competitionId: UUID): List<Viewer> =
        Viewer
            .find { ViewerTable.competitionId eq competitionId }
            .toList()

    override suspend fun create(init: Viewer.() -> Unit): Viewer =
        Viewer.new(init)
}