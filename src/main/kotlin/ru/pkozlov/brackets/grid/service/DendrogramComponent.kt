package ru.pkozlov.brackets.grid.service

import ru.pkozlov.brackets.app.utils.toDeque
import ru.pkozlov.brackets.grid.dto.Node
import ru.pkozlov.brackets.grid.dto.Participant
import ru.pkozlov.brackets.participant.dto.ParticipantDto
import java.util.*
import kotlin.math.log2

object DendrogramComponent {
    fun createAndFill(participants: List<ParticipantDto>): List<Node> {
        if (participants.isEmpty()) return emptyList()

        val dendrogramSize: Int = defineDendrogramSize(participants.size)
        val penultimateLevelCapacity: Int = dendrogramSize - participants.size
        val (dendrogram, flatDendrogram) = createGraph(dendrogramSize)

        return when (participants.size) {
            3 -> fillByCircleSystem(participants)
            else ->
                fillByOlympicSystem(flatDendrogram, penultimateLevelCapacity, participants).run { listOf(dendrogram) }
        }
    }

    private fun fillByOlympicSystem(
        flatGraph: NavigableMap<Int, Deque<Node>>,
        penultimateLevelCapacity: Int,
        participants: Collection<ParticipantDto>
    ) {
        val teams: Queue<Queue<ParticipantDto>> =
            participants
                .groupBy { it.team }
                .map { (_, participantsOfOneTeam) -> participantsOfOneTeam.shuffled().toDeque() }
                .sortedByDescending { it.size }
                .toDeque()

        val preLastLevel: Deque<Node> = flatGraph.run { getOrElse(lastKey() - 1) { LinkedList() } }

        processPreLastLevel(penultimateLevelCapacity, preLastLevel, teams)
        processLastLevel(preLastLevel, teams)
    }

    private fun fillByCircleSystem(
        participants: Collection<ParticipantDto>
    ): List<Node> {
        val participantsQueue: Queue<ParticipantDto> = participants.toDeque()
        val result: List<Node> = buildList {
            repeat(3) {
                Node(
                    id = UUID.randomUUID(),
                    right = Node(id = UUID.randomUUID()),
                    left = Node(id = UUID.randomUUID())
                ).run(::add)
            }
        }

        return result.onEach { node ->
            val leftIteratingParticipant = participantsQueue.poll()
            node.left?.participant = Participant(
                id = leftIteratingParticipant.id,
                firstName = leftIteratingParticipant.firstName,
                lastName = leftIteratingParticipant.lastName,
                middleName = leftIteratingParticipant.middleName,
                team = leftIteratingParticipant.team,
                winner = false
            )
            participantsQueue.add(leftIteratingParticipant)

            val rightIteratingParticipant = participantsQueue.poll()
            node.right?.participant = Participant(
                id = rightIteratingParticipant.id,
                firstName = rightIteratingParticipant.firstName,
                lastName = rightIteratingParticipant.lastName,
                middleName = rightIteratingParticipant.middleName,
                team = rightIteratingParticipant.team,
                winner = false
            )
            participantsQueue.add(rightIteratingParticipant)
        }
    }

    private fun processPreLastLevel(
        penultimateLevelCapacity: Int,
        penultimateLevel: Deque<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        repeat(penultimateLevelCapacity) {
            val iteratingParticipant = teams.pollAndAddToTail()
            penultimateLevel.pollLast().apply {
                participant = Participant(
                    id = iteratingParticipant.id,
                    firstName = iteratingParticipant.firstName,
                    lastName = iteratingParticipant.lastName,
                    middleName = iteratingParticipant.middleName,
                    team = iteratingParticipant.team,
                    winner = false
                )
            }
        }
    }

    private fun processLastLevel(
        preLastLevel: Deque<Node>,
        teams: Queue<Queue<ParticipantDto>>
    ) {
        while (preLastLevel.isNotEmpty()) {
            preLastLevel.pollLast().apply {
                left?.participant = teams.pollAndAddToTail().run {
                    Participant(
                        id = id,
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        team = team,
                        winner = false
                    )
                }
                right?.participant = teams.pollAndAddToTail().run {
                    Participant(
                        id = id,
                        firstName = firstName,
                        lastName = lastName,
                        middleName = middleName,
                        team = team,
                        winner = false
                    )
                }
            }
        }
    }

    private fun defineDendrogramSize(participantsSize: Int): Int =
        when {
            participantsSize <= 2 -> 2
            participantsSize == 3 -> 8
            participantsSize == 4 -> 4
            participantsSize <= 8 -> 8
            participantsSize <= 16 -> 16
            participantsSize <= 32 -> 32
            else -> throw IllegalArgumentException("Count of participants is $participantsSize. Max is 32.")
        }

    private fun createGraph(dendrogramSize: Int): Pair<Node, NavigableMap<Int, Deque<Node>>> {
        val flatGraph: NavigableMap<Int, Deque<Node>> = TreeMap()
        val lastLevel: Int = log2(dendrogramSize.toDouble()).toInt()
        val graph: Node = createNode(currentLevel = 0, lastLevel = lastLevel, accumulator = flatGraph)
        return graph to flatGraph
    }

    private fun createNode(currentLevel: Int, lastLevel: Int, accumulator: MutableMap<Int, Deque<Node>>): Node =
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