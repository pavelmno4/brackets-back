package ru.pkozlov.brackets.grid.util

import java.util.*

inline fun <T, K> Iterable<T>.groupQueueBy(keySelector: (T) -> K): Map<K, Queue<T>> {
    return groupQueueByTo(LinkedHashMap<K, Queue<T>>(), keySelector)
}

inline fun <T, K, M : MutableMap<in K, Queue<T>>> Iterable<T>.groupQueueByTo(destination: M, keySelector: (T) -> K): M {
    for (element in this) {
        val key = keySelector(element)
        val list = destination.getOrPut(key) { LinkedList() }
        list.add(element)
    }
    return destination
}

fun <T> Collection<T>.toQueue(): Queue<T> = LinkedList(this)

fun <T> Queue<Queue<T>>.pollAndAddLast(): T {
    val queue: Queue<T> = poll()
    val element: T = queue.poll()
    if (queue.isNotEmpty()) add(queue)
    return element
}