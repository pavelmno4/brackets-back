package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class PatchNodeWinnerDto(
    @Serializable(UUIDSerializer::class)
    val winnerNodeId: UUID
)
