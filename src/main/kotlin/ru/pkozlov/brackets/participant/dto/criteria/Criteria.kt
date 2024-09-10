package ru.pkozlov.brackets.participant.dto.criteria

sealed interface Criteria<T> {
    val value: T
}