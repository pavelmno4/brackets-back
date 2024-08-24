package ru.pkozlov.brackets.app.utils.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.pkozlov.brackets.app.dto.AgeCategory

object AgeCategorySerializer : KSerializer<AgeCategory> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("AgeCategory", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AgeCategory) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): AgeCategory {
        return AgeCategory(decoder.decodeString())
    }
}