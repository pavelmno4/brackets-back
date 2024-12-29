package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class NodeView(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val data: Data?
) {
    @Serializable
    data class Data(
        @Serializable(UUIDSerializer::class)
        val participantId: UUID,
        val winner: Boolean
    )
}
