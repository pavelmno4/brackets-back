package ru.pkozlov.brackets.grid.service

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.grid.dto.CreateGridDto
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.repository.GridRepository
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.GenderCriteria
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.util.*

class GridService(
    private val gridRepository: GridRepository,
    private val participantService: ParticipantService,
    private val dendrogramComponent: DendrogramComponent
) {
    private val logger: Logger = LoggerFactory.getLogger(GridService::class.java)

    suspend fun generate(competitionId: UUID): List<GridDto> {
        val generationScope = CoroutineScope(Dispatchers.Default)
        val categories: Map<Pair<AgeCategory, WeightCategory>, List<ParticipantDto>> =
            participantService
                .findAllByCriteria(competitionId, setOf(GenderCriteria(Gender.MALE)))
                .filter { participant -> participant.weight != null }
                .groupBy { it.ageCategory to it.weightCategory }

        val grids: List<GridDto> = categories
            .map { (category, participants) ->
                generationScope.async {
                    val (ageCategory, weightCategory) = category

                    CreateGridDto(
                        competitionId = competitionId,
                        gender = Gender.MALE,
                        ageCategory = ageCategory,
                        weightCategory = weightCategory,
                        dendrogram = dendrogramComponent.createAndFill(participants)
                    )
                        .let { grid -> gridRepository.create(grid) }
                        .also { logger.info("$ageCategory $weightCategory grid created") }
                }
            }
            .awaitAll()
        generationScope.cancel()

        return grids
    }
}