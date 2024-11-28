package ru.pkozlov.brackets.app.utils

import java.util.*

fun <T> Collection<T>.toQueue(): Queue<T> = LinkedList(this)