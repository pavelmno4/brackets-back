package ru.pkozlov.brackets.grid.mapper

import ru.pkozlov.brackets.grid.domain.Grid
import ru.pkozlov.brackets.grid.dto.*
import java.util.*

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
    nodes = dendrogram?.asNodesList() ?: emptyList(),
    edges = dendrogram?.asEdgeList() ?: emptyList()
)

private fun Node.asNodesList(): List<NodeView> {
    val result: MutableList<NodeView> = mutableListOf()
    val level: Queue<Node> = LinkedList()
    level.add(this)

    while (level.isNotEmpty()) {
        val node: Node = level.poll()

        val nodeView = NodeView(
            id = node.id,
            data = node.participant?.run {
                NodeView.Data(
                    participantFullName = fullName,
                    team = team
                )
            }
        )
        result.add(nodeView)

        node.left?.run(level::add)
        node.right?.run(level::add)

    }
    return result
}

private fun Node.asEdgeList(): List<EdgeView> {
    val result: MutableList<EdgeView> = mutableListOf()
    val level: Queue<Node> = LinkedList()
    level.add(this)

    while (level.isNotEmpty()) {
        val node: Node = level.poll()

        val leftEdgeView = node.left?.let { leftChild ->
            EdgeView(
                id = "${leftChild.id}-${node.id}",
                source = leftChild.id,
                target = node.id
            )
        }

        val rightEdgeView = node.right?.let { rightChild ->
            EdgeView(
                id = "${rightChild.id}-${node.id}",
                source = rightChild.id,
                target = node.id
            )
        }

        leftEdgeView?.run(result::add)
        rightEdgeView?.run(result::add)

        node.left?.run(level::add)
        node.right?.run(level::add)
    }
    return result
}