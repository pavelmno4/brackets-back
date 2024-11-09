package ru.pkozlov.brackets.grid.dto

import java.util.*

data class Node(
    val level: Level,
    val left: Node? = null,
    val right: Node? = null,
    var participant: ParticipantDto? = null
) {
    enum class Level(val value: Int) {
        ZERO(value = 0),
        ONE(value = 1),
        TWO(value = 2),
        THREE(value = 3),
        FOUR(value = 4),
        FIVE(value = 5);

        fun next(): Level = Companion.valueOf(value + 1)
        fun previous(): Level = Companion.valueOf(value - 1)

        companion object Companion {
            fun valueOf(integer: Int): Level =
                if (integer <= 0) ZERO
                else entries.firstOrNull { it.value == integer } ?: throw IllegalArgumentException()

            val comporator: Comparator<Level> = compareBy(Level::value)
        }
    }

    fun <K, M : MutableMap<K, LinkedList<Node>>> flat(destination: M, keySelector: (Node) -> K): M {
        val key = keySelector(this)
        val list = destination.getOrPut(key) { LinkedList() }
        list.add(this)

        left?.flat(destination, keySelector)
        right?.flat(destination, keySelector)

        return destination
    }
}