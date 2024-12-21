package ru.pkozlov.brackets.competition.dto.category

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesByGender(
    val male: List<Category>,
    val female: List<Category>
)
