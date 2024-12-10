package ru.pkozlov.brackets.grid.service

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.bfs
import ru.pkozlov.brackets.grid.dto.*
import ru.pkozlov.brackets.grid.repository.GridRepository
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.GenderCriteria
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.util.*

class GridService(
    private val gridRepository: GridRepository,
    private val participantService: ParticipantService
) {
    private val logger: Logger = LoggerFactory.getLogger(GridService::class.java)

    suspend fun generate(competitionId: UUID): List<GridDto> {
        val generationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        val categories: Map<Pair<AgeCategory, WeightCategory>, List<ParticipantDto>> =
            participantService
                .findAllByCriteria(competitionId, setOf(GenderCriteria(Gender.MALE)))
                .filter { participant -> participant.weight != null }
                .groupBy { it.ageCategory to it.weightCategory }
        gridRepository.deleteAllWith(competitionId)

        val grids: List<GridDto> = categories
            .map { (category, participants) ->
                generationScope.async {
                    val (ageCategory, weightCategory) = category

                    CreateGridDto(
                        competitionId = competitionId,
                        gender = Gender.MALE,
                        ageCategory = ageCategory,
                        weightCategory = weightCategory,
                        dendrogram = DendrogramComponent.createAndFill(participants)
                    )
                        .let { grid -> gridRepository.create(grid) }
                        .also { logger.info("$ageCategory $weightCategory grid created") }
                }
            }
            .awaitAll()
        generationScope.cancel()

        return grids
    }

    suspend fun setWinnerForNode(gridId: UUID, nodeId: UUID, winnerNodeId: UUID): GridDto? =
        gridRepository.update(gridId) { grid ->
            val updatedDendrogram = grid.dendrogram.map { root ->
                val targetNode: Node? = root
                    .bfs { result, node -> if (node.id == nodeId) result.add(node) }
                    .firstOrNull()

                val winner: Participant? = root
                    .bfs { result, node -> if (node.id == winnerNodeId) result.add(node.participant) }
                    .firstOrNull()

                targetNode?.apply {
                    participant = winner
                }
                root
            }
            grid.dendrogram = emptyList()           // for update object link
            grid.dendrogram = updatedDendrogram
        }

    suspend fun patchMedalists(gridId: UUID, patchMedalists: PatchGridMedalistsDto): GridDto? =
        gridRepository.update(gridId) { grid ->
            grid.firstPlaceParticipantId = patchMedalists.firstPlaceParticipantId
            grid.secondPlaceParticipantId = patchMedalists.secondPlaceParticipantId
            grid.thirdPlaceParticipantId = patchMedalists.thirdPlaceParticipantId
        }

    suspend fun findBy(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): GridDto? =
        gridRepository.findBy(
            competitionId = competitionId,
            gender = gender,
            ageCategory = ageCategory,
            weightCategory = weightCategory
        )
}