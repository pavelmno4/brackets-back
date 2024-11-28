package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.UUID

@Serializable
data class Node(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    var participant: Participant? = null,
    val left: Node? = null,
    val right: Node? = null
)
