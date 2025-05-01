package ru.pkozlov.brackets.app.utils

import ru.pkozlov.brackets.grid.dto.Node
import java.util.*

fun <T> Collection<T>.toDeque(): Deque<T> = LinkedList(this)

fun <T> Node.bfs(operation: (result: MutableList<T>, node: Node) -> Unit): List<T> {
    val result: MutableList<T> = mutableListOf()
    val level: Queue<Node> = LinkedList()
    level.add(this)

    while (level.isNotEmpty()) {
        val node: Node = level.poll()

        operation(result, node)

        node.left?.run(level::add)
        node.right?.run(level::add)
    }
    return result
}

fun Node.flat(): Map<Int, List<Node>> {
    val result: MutableMap<Int, LinkedList<Node>> = mutableMapOf()
    val level: Queue<Node> = LinkedList()
    level.add(this)

    var levelIndex: Int = 0
    while (level.isNotEmpty()) {
        val levelSize: Int = level.size

        repeat(levelSize) {
            val resultLevel: MutableList<Node> = result.getOrPut(levelIndex) { LinkedList() }
            val node: Node = level.poll()

            resultLevel.add(node)

            node.left?.run(level::add)
            node.right?.run(level::add)
        }
        levelIndex++
    }
    return result
}