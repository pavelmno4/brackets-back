package ru.pkozlov.brackets.grid.service

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.common.exception.NotFoundException
import ru.pkozlov.brackets.competition.dto.competition.CompetitionDto
import ru.pkozlov.brackets.competition.service.CompetitionService
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.dto.ParticipantDto
import ru.pkozlov.brackets.grid.mapper.asGridParticipant
import ru.pkozlov.brackets.participant.dto.criteria.GenderCriteria
import ru.pkozlov.brackets.participant.enumeration.Gender
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.io.BufferedOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Inherits code from [brackets-excel](https://github.com/pavelmno4/brackets-excel)
 * */
class GridService(
    private val gridGenerationComponent: GridGenerationComponent,
    private val competitionService: CompetitionService,
    private val participantService: ParticipantService,
    private val templateComponent: TemplateComponent
) {
    suspend fun generate(competitionId: UUID, outputStream: BufferedOutputStream) {
        val competition: CompetitionDto = competitionService.findById(competitionId)
            ?: throw NotFoundException("Competition with id $competitionId not found")

        val participants: Map<AgeCategory, Map<WeightCategory, List<ParticipantDto>>> =
            participantService
                .findAllByCriteria(competitionId, setOf(GenderCriteria(Gender.MALE)))
                .filter { participant -> participant.weight != null }
                .groupBy { it.ageCategory }
                .mapValues { (_, participants) ->
                    participants.groupBy({ it.weightCategory }, { it.asGridParticipant() })
                }

        val grids: Map<AgeCategory, List<GridDto>> = participants.mapValues { (ageCategory, weightCategoryMap) ->
            weightCategoryMap.map { (weightCategory, participants) ->
                gridGenerationComponent.generate(
                    tournamentName = competition.title,
                    ageCategory = ageCategory,
                    weightCategory = weightCategory,
                    participants = participants
                )
            }
        }

        ZipOutputStream(outputStream).use { zipOut ->
            grids.forEach { (ageCategory, grids) ->
                val zipEntry = ZipEntry("${ageCategory.value}.xlsx")
                zipOut.putNextEntry(zipEntry)
                zipOut.write(templateComponent.process(grids))
                zipOut.closeEntry()
            }
        }
    }
}