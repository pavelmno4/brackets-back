package ru.pkozlov.brackets.grid.service

import ru.pkozlov.brackets.app.dto.AgeCategory
import ru.pkozlov.brackets.app.dto.WeightCategory
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.dto.Node
import ru.pkozlov.brackets.grid.dto.Node.Level
import ru.pkozlov.brackets.grid.dto.ParticipantDto
import ru.pkozlov.brackets.grid.util.*
import java.util.*
import kotlin.math.log2

/**
 * Inherits code from [brackets-excel](https://github.com/pavelmno4/brackets-excel)
 * */
class GridGenerationComponent {
    fun generate(
        tournamentName: String,
        ageCategory: AgeCategory,
        weightCategory: WeightCategory,
        participants: Collection<ParticipantDto>
    ): GridDto {
        if (participants.isEmpty()) return emptyGrid

        val bracketSize: Int = defineBracketSize(participants.size)
        val preLastLevelCapacity: Int = bracketSize - participants.size
        val templatePath: String = defineTemplatePath(participants.size)
        val graph: Node = createGraph(bracketSize)

        when (participants.size) {
            3 -> fillCircleGrid(graph, participants)
            else -> fillOlympicGrid(graph, preLastLevelCapacity, participants)
        }

        return GridDto(
            tournamentName = tournamentName,
            ageCategory = ageCategory,
            weightCategory = weightCategory,
            templatePath = templatePath,
            graph = graph
        )
    }

    private fun fillOlympicGrid(
        graph: Node,
        preLastLevelCapacity: Int,
        participants: Collection<ParticipantDto>
    ) {
        val teams: Queue<Queue<ParticipantDto>> =
            participants
                .groupQueueBy { it.team }.values
                .sortedByDescending { it.size }
                .toQueue()

        val flatGraph: TreeMap<Level, LinkedList<Node>> =
            graph.flat(TreeMap<Level, LinkedList<Node>>(Level.comporator)) { it.level }

        val preLastLevel: Queue<Node> = flatGraph.run { get(lastKey().previous()) ?: LinkedList() }

        processPreLastLevel(preLastLevelCapacity, preLastLevel, teams)
        processLastLevel(preLastLevel, teams)
    }

    private fun fillCircleGrid(
        graph: Node,
        participants: Collection<ParticipantDto>
    ) {
        val flatGraph: TreeMap<Level, LinkedList<Node>> =
            graph.flat(TreeMap<Level, LinkedList<Node>>(Level.comporator)) { it.level }

        val lastLevel: Queue<Node> = flatGraph.lastEntry().value
        val participantsQueue: Queue<ParticipantDto> = participants.toQueue()

        repeat(6) {
            lastLevel.poll().apply {
                val part: ParticipantDto = participantsQueue.poll()
                participant = part
                participantsQueue.add(part)
            }
        }
    }

    private fun processPreLastLevel(
        preLastLevelCapacity: Int,
        preLastLevel: Queue<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        repeat(preLastLevelCapacity) {
            preLastLevel.poll().apply { participant = teams.pollAndAddLast() }
        }
    }

    private fun processLastLevel(
        preLastLevel: Queue<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        while (preLastLevel.isNotEmpty()) {
            preLastLevel.poll().apply {
                left?.participant = teams.pollAndAddLast()
                right?.participant = teams.pollAndAddLast()
            }
        }
    }

    private fun defineBracketSize(participantsSize: Int): Int =
        when {
            participantsSize <= 2 -> 2
            participantsSize == 3 -> 8
            participantsSize == 4 -> 4
            participantsSize <= 8 -> 8
            participantsSize <= 16 -> 16
            else -> throw IllegalArgumentException("Count of participants is $participantsSize. Max grid is 16.")
        }

    private fun defineTemplatePath(participantsSize: Int): String =
        when {
            participantsSize == 1 -> TEMPLATE_PATH_1
            participantsSize == 2 -> TEMPLATE_PATH_2
            participantsSize == 3 -> TEMPLATE_PATH_3
            participantsSize == 4 -> TEMPLATE_PATH_4
            participantsSize <= 8 -> TEMPLATE_PATH_8
            participantsSize <= 16 -> TEMPLATE_PATH_16
            else -> throw IllegalArgumentException("Count of participants is $participantsSize. Max grid is 16.")
        }

    private fun createGraph(bracketSize: Int): Node {
        val deepLevel: Level = Level.valueOf(log2(bracketSize.toDouble()).toInt())
        return createNode(Level.ZERO, deepLevel)
    }

    private fun createNode(currentLevel: Level, deepLevel: Level): Node =
        if (currentLevel == deepLevel) Node(level = currentLevel)
        else Node(
            level = currentLevel,
            left = createNode(currentLevel.next(), deepLevel),
            right = createNode(currentLevel.next(), deepLevel)
        )

    private val emptyGrid: GridDto = GridDto(
        tournamentName = "-",
        ageCategory = AgeCategory("-"),
        weightCategory = WeightCategory("-"),
        templatePath = EMPTY_TEMPLATE_PATH,
        graph = Node(Level.ZERO)
    )
}