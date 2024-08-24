package ru.pkozlov.brackets.app.utils.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.pkozlov.brackets.app.dto.WeightCategory

object WeightCategorySerializer : KSerializer<WeightCategory> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("WeightCategory", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: WeightCategory) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): WeightCategory {
        return WeightCategory(decoder.decodeString())
    }
}