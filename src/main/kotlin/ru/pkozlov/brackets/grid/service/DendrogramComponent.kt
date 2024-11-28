package ru.pkozlov.brackets.grid.service

import ru.pkozlov.brackets.app.utils.toQueue
import ru.pkozlov.brackets.grid.dto.Node
import ru.pkozlov.brackets.grid.dto.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import java.util.*
import kotlin.math.log2

class DendrogramComponent {
    fun createAndFill(participants: List<ParticipantDto>): Node? {
        if (participants.isEmpty()) return null

        val dendrogramSize: Int = defineDendrogramSize(participants.size)
        val penultimateLevelCapacity: Int = dendrogramSize - participants.size
        val (dendrogram, flatDendrogram) = createGraph(dendrogramSize)

        when (participants.size) {
            3 -> fillByCircleSystem(flatDendrogram, participants)
            else -> fillByOlympicSystem(flatDendrogram, penultimateLevelCapacity, participants)
        }
        return dendrogram
    }

    private fun fillByOlympicSystem(
        flatGraph: NavigableMap<Int, Queue<Node>>,
        penultimateLevelCapacity: Int,
        participants: Collection<ParticipantDto>
    ) {
        val teams: Queue<Queue<ParticipantDto>> =
            participants
                .groupBy { it.team }
                .map { (_, teams) -> teams.shuffled().toQueue() }
                .sortedByDescending { it.size }
                .toQueue()

        val preLastLevel: Queue<Node> = flatGraph.run { getOrElse(lastKey() - 1) { LinkedList() } }

        processPreLastLevel(penultimateLevelCapacity, preLastLevel, teams)
        processLastLevel(preLastLevel, teams)
    }

    private fun fillByCircleSystem(
        flatGraph: NavigableMap<Int, Queue<Node>>,
        participants: Collection<ParticipantDto>
    ) {
        val lastLevel: Queue<Node> = flatGraph.lastEntry().value
        val participantsQueue: Queue<ParticipantDto> = participants.toQueue()

        repeat(6) {
            lastLevel.poll().apply {
                val iteratingParticipant = participantsQueue.poll()
                participant = Participant(
                    fullName = iteratingParticipant.fullName,
                    team = iteratingParticipant.team
                )
                participantsQueue.add(iteratingParticipant)
            }
        }
    }

    private fun processPreLastLevel(
        penultimateLevelCapacity: Int,
        penultimateLevel: Queue<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        repeat(penultimateLevelCapacity) {
            val iteratingParticipant = teams.pollAndAddToTail()
            penultimateLevel.poll().apply {
                participant = Participant(
                    fullName = iteratingParticipant.fullName,
                    team = iteratingParticipant.team
                )
            }
        }
    }

    private fun processLastLevel(
        preLastLevel: Queue<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        while (preLastLevel.isNotEmpty()) {
            preLastLevel.poll().apply {
                left?.participant = teams.pollAndAddToTail().run {
                    Participant(
                        fullName = fullName,
                        team = team
                    )
                }
                right?.participant = teams.pollAndAddToTail().run {
                    Participant(
                        fullName = fullName,
                        team = team
                    )
                }
            }
        }
    }

    private fun defineDendrogramSize(participantsSize: Int): Int =
        when {
            participantsSize <= 2 -> 2
            participantsSize == 3 -> 3
            participantsSize == 4 -> 4
            participantsSize <= 8 -> 8
            participantsSize <= 16 -> 16
            participantsSize <= 32 -> 32
            else -> throw IllegalArgumentException("Count of participants is $participantsSize. Max is 32.")
        }

    private fun createGraph(dendrogramSize: Int): Pair<Node, NavigableMap<Int, Queue<Node>>> {
        val flatGraph: NavigableMap<Int, Queue<Node>> = TreeMap()
        val lastLevel: Int = log2(dendrogramSize.toDouble()).toInt()
        val graph: Node = createNode(currentLevel = 0, lastLevel = lastLevel, accumulator = flatGraph)
        return graph to flatGraph
    }

    private fun createNode(currentLevel: Int, lastLevel: Int, accumulator: MutableMap<Int, Queue<Node>>): Node =
        if (currentLevel == lastLevel)
            Node(id = UUID.randomUUID()).also { node -> accumulator.getOrPut(currentLevel) { LinkedList() }.add(node) }
        else Node(
            id = UUID.randomUUID(),
            left = createNode(currentLevel + 1, lastLevel, accumulator),
            right = createNode(currentLevel + 1, lastLevel, accumulator)
        ).also { node -> accumulator.getOrPut(currentLevel) { LinkedList() }.add(node) }

    private fun Queue<Queue<ParticipantDto>>.pollAndAddToTail(): ParticipantDto {
        val team: Queue<ParticipantDto> = poll()
        val participant: ParticipantDto = team.poll()
        if (team.isNotEmpty()) add(team)
        return participant
    }
}