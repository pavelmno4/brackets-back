package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class EdgeView(
    val id: String,
    @Serializable(UUIDSerializer::class)
    val source: UUID,
    @Serializable(UUIDSerializer::class)
    val target: UUID
)
