package ru.pkozlov.brackets.grid.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pkozlov.brackets.app.config.FilesConfig
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
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists


/**
 * Inherits code from [brackets-excel](https://github.com/pavelmno4/brackets-excel)
 * */
class GridService(
    private val gridGenerationComponent: GridGenerationComponent,
    private val competitionService: CompetitionService,
    private val participantService: ParticipantService,
    private val templateComponent: TemplateComponent,
    private val filesConfig: FilesConfig
) {
    private val logger: Logger = LoggerFactory.getLogger(GridService::class.java)

    suspend fun generate(competitionId: UUID) {
        val competition: CompetitionDto = competitionService.findById(competitionId)
            ?: throw NotFoundException("Competition with id $competitionId not found")

        logger.info("Grid generation started...")

        val participants: Map<AgeCategory, Map<WeightCategory, List<ParticipantDto>>> =
            participantService
                .findAllByCriteria(competitionId, setOf(GenderCriteria(Gender.MALE)))
                .filter { participant -> participant.weight != null }
                .groupBy { it.ageCategory }
                .mapValues { (_, participants) ->
                    participants
                        .groupBy({ it.weightCategory }, { it.asGridParticipant() })
                        .toSortedMap(compareBy { it.value })
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

        withContext(Dispatchers.IO) {
            createOutputStream(competitionId).use { file ->
                ZipOutputStream(file).use { zipOutput ->
                    grids.forEach { (ageCategory, grids) ->
                        val zipEntry = ZipEntry("${ageCategory.value}.xlsx")
                        zipOutput.putNextEntry(zipEntry)
                        zipOutput.write(templateComponent.process(grids))
                        zipOutput.closeEntry()
                    }
                }
            }
        }

        logger.info("Grid generation completed!")
    }

    private fun createOutputStream(competitionId: UUID): FileOutputStream =
        Path(filesConfig.output).takeIf { it.exists() }
            ?.run { FileOutputStream("${filesConfig.output}/competition_grid_$competitionId.zip") }
            ?: Path(filesConfig.output).createDirectory()
                .run { FileOutputStream("${filesConfig.output}/competition_grid_$competitionId.zip") }
}