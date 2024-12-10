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