package ru.pkozlov.brackets.grid.mapper

import ru.pkozlov.brackets.app.utils.bfs
import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.dto.*

fun Grid.asDto(): GridDto = GridDto(
    id = id.value,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    dendrogram = dendrogram,
    competitionId = competitionId,
    firstPlaceParticipantId = firstPlaceParticipantId,
    secondPlaceParticipantId = secondPlaceParticipantId,
    thirdPlaceParticipantId = thirdPlaceParticipantId,
    show = show
)

fun GridDto.asView(): GridView = GridView(
    id = id,
    gender = gender,
    ageCategory = ageCategory,
    weightCategory = weightCategory,
    nodes = dendrogram.flatMap { it.asNodesList() },
    edges = dendrogram.flatMap { it.asEdgeList() },
    firstPlaceParticipantId = firstPlaceParticipantId,
    secondPlaceParticipantId = secondPlaceParticipantId,
    thirdPlaceParticipantId = thirdPlaceParticipantId,
    show = show
)

private fun Node.asNodesList(): List<NodeView> =
    bfs { result, node ->
        NodeView(
            id = node.id,
            data = node.participant?.run {
                NodeView.Data(
                    participantId = id,
                    winner = winner
                )
            }
        ).run(result::add)
    }

private fun Node.asEdgeList(): List<EdgeView> =
    bfs { result, node ->
        node.left?.let { leftChild ->
            EdgeView(
                id = "${leftChild.id}-${node.id}",
                source = leftChild.id,
                target = node.id
            ).run(result::add)
        }
        node.right?.let { rightChild ->
            EdgeView(
                id = "${rightChild.id}-${node.id}",
                source = rightChild.id,
                target = node.id
            ).run(result::add)
        }
    }