package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class Participant(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val fullName: String,
    val team: String
)
