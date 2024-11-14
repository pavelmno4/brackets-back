package ru.pkozlov.brackets.app.config

import kotlinx.serialization.Serializable

@Serializable
data class FilesConfig(
    val output: String
)
