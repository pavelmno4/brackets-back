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
    competitionId = competitionId
)

fun GridDto.asView(): GridView = GridView(
    id = id,
    nodes = dendrogram.flatMap { it.asNodesList() },
    edges = dendrogram.flatMap { it.asEdgeList() }
)

private fun Node.asNodesList(): List<NodeView> =
    bfs { result, node ->
        NodeView(
            id = node.id,
            data = node.participant?.run {
                NodeView.Data(
                    participantFullName = fullName,
                    team = team
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