package ru.pkozlov.brackets.viewer.service

import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.viewer.domain.Viewer
import ru.pkozlov.brackets.viewer.dto.CreateViewerDto
import ru.pkozlov.brackets.viewer.dto.ViewerDto
import ru.pkozlov.brackets.viewer.mapper.asDto
import ru.pkozlov.brackets.viewer.repository.ViewerRepository
import java.util.*

class ViewerService(
    private val viewerRepository: ViewerRepository
) {
    suspend fun create(competitionId: UUID, viewer: CreateViewerDto): ViewerDto =
        suspendTransaction {
            viewerRepository.create {
                firstName = viewer.firstName.trim()
                lastName = viewer.lastName.trim()
                middleName = viewer.middleName.trim()
                phone = viewer.phone.trim()
                this.competitionId = competitionId
            }.asDto()
        }

    suspend fun findAll(competitionId: UUID): List<ViewerDto> =
        suspendTransaction {
            viewerRepository.findAllByCompetitionId(competitionId).map(Viewer::asDto)
        }
}