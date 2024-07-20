package ru.pkozlov.competition.dto.category

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val yearRange: String,
    val weights: Set<String>
)