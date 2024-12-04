package ru.pkozlov.brackets.grid.dto

import kotlinx.serialization.Serializable
import ru.pkozlov.brackets.app.utils.serializer.UUIDSerializer
import java.util.*

@Serializable
data class GridView(
    @Serializable(UUIDSerializer::class)
    val id: UUID,
    val nodes: List<NodeView>,
    val edges: List<EdgeView>
)
