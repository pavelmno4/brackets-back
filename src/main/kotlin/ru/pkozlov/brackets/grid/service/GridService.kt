package ru.pkozlov.brackets.grid.service

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.app.enumeration.Gender
import ru.pkozlov.brackets.app.utils.bfs
import ru.pkozlov.brackets.app.utils.suspendTransaction
import ru.pkozlov.brackets.grid.dto.*
import ru.pkozlov.brackets.grid.mapper.asDto
import ru.pkozlov.brackets.grid.repository.GridRepository
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import ru.pkozlov.brackets.participant.dto.criteria.AgeCategoryCriteria
import ru.pkozlov.brackets.participant.dto.criteria.GenderCriteria
import ru.pkozlov.brackets.participant.dto.criteria.WeightCategoryCriteria
import ru.pkozlov.brackets.participant.service.ParticipantService
import java.util.*

class GridService(
    private val gridRepository: GridRepository,
    private val participantService: ParticipantService
) {
    private val logger: Logger = LoggerFactory.getLogger(GridService::class.java)

    suspend fun generateAutomatically(competitionId: UUID): List<GridDto> = suspendTransaction {
        val categories: Map<Pair<AgeCategory, WeightCategory>, List<ParticipantDto>> =
            participantService
                .findAllByCriteria(competitionId, setOf(GenderCriteria(Gender.MALE)))
                .filter { participant -> participant.weight != null }
                .groupBy { it.ageCategory to it.weightCategory }
        gridRepository.deleteAllWith(competitionId)

        generate(competitionId, Gender.MALE, categories)
    }

    suspend fun generateForSingleCategory(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): GridDto? = suspendTransaction {
        val category: Map<Pair<AgeCategory, WeightCategory>, List<ParticipantDto>> =
            participantService
                .findAllByCriteria(
                    competitionId = competitionId,
                    criteria = listOf(
                        GenderCriteria(gender),
                        AgeCategoryCriteria(ageCategory),
                        WeightCategoryCriteria(weightCategory)
                    )
                )
                .filter { participant -> participant.weight != null }
                .let { participants -> mapOf(Pair(ageCategory, weightCategory) to participants) }

        gridRepository.deleteByGenderAgeAndWeightCategory(competitionId, gender, ageCategory, weightCategory)

        generate(competitionId, gender, category).firstOrNull()
    }

    suspend fun setWinnerForNode(gridId: UUID, nodeId: UUID, winnerNodeId: UUID): GridDto? = suspendTransaction {
        gridRepository.update(gridId) { grid ->
            val updatedDendrogram = grid.dendrogram.map { root ->
                val targetNode: Node? = root
                    .bfs { result, node -> if (node.id == nodeId) result.add(node) }
                    .firstOrNull()

                val winner: Participant? = targetNode?.run {
                    if (left?.id == winnerNodeId) {
                        right?.participant = right?.participant?.copy(winner = false)
                        left.participant = left.participant?.copy(winner = true)
                        left.participant
                    } else if (right?.id == winnerNodeId) {
                        left?.participant = left?.participant?.copy(winner = false)
                        right.participant = right.participant?.copy(winner = true)
                        right.participant
                    } else null
                }

                targetNode?.apply {
                    participant = winner?.copy(winner = false)
                }
                root
            }
            grid.dendrogram = emptyList()           // for update object link
            grid.dendrogram = updatedDendrogram
        }?.asDto()
    }

    suspend fun patchMedalists(gridId: UUID, patchMedalists: PatchGridMedalistsDto): GridDto? = suspendTransaction {
        gridRepository.update(gridId) { grid ->
            grid.firstPlaceParticipantId = patchMedalists.firstPlaceParticipantId
            grid.secondPlaceParticipantId = patchMedalists.secondPlaceParticipantId
            grid.thirdPlaceParticipantId = patchMedalists.thirdPlaceParticipantId
        }?.asDto()
    }

    suspend fun swapNodes(gridId: UUID, swapNodes: PatchGridSwapNodesDto): GridDto? = suspendTransaction {
        gridRepository.update(gridId) { grid ->
            val updatedDendrogram = grid.dendrogram.map { root ->
                val (firstNode, secondNode) = root
                    .bfs { result, node ->
                        if (node.id == swapNodes.firstNodeId || node.id == swapNodes.secondNodeId)
                            result.add(node)
                    }
                    .run { getOrNull(0) to getOrNull(1) }

                val firstNodeOriginalParticipant = firstNode?.participant

                firstNode?.apply {
                    participant = secondNode?.participant
                }
                secondNode?.apply {
                    participant = firstNodeOriginalParticipant
                }
                root
            }
            grid.dendrogram = emptyList()           // for update object link
            grid.dendrogram = updatedDendrogram
        }?.asDto()
    }

    suspend fun findBy(
        competitionId: UUID,
        gender: Gender,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory
    ): GridDto? = suspendTransaction {
        gridRepository.findBy(
            competitionId = competitionId,
            gender = gender,
            ageCategory = ageCategory,
            weightCategory = weightCategory
        )?.asDto()
    }

    private suspend fun generate(
        competitionId: UUID,
        gender: Gender,
        categories: Map<Pair<AgeCategory, WeightCategory>, List<ParticipantDto>>
    ): List<GridDto> = suspendTransaction {
        val generationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

        val grids: List<GridDto> = categories
            .map { (category, participants) ->
                generationScope.async {
                    val (ageCategory, weightCategory) = category

                    suspendTransaction {
                        gridRepository.create {
                            this.competitionId = competitionId
                            this.gender = gender
                            this.ageCategory = ageCategory
                            this.weightCategory = weightCategory
                            this.dendrogram = DendrogramComponent.createAndFill(participants)
                        }.asDto()
                    }.also { logger.info("$gender $ageCategory $weightCategory grid created") }
                }
            }
            .awaitAll()
        generationScope.cancel()

        grids
    }
}