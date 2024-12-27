package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class PatchGridSwapNodesDto(
    @Serializable(UUIDSerializer::class)
    val firstNodeId: UUID,
    @Serializable(UUIDSerializer::class)
    val secondNodeId: UUID
)
