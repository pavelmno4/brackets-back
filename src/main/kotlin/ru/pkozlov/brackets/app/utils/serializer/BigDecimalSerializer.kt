package ru.pkozlov.brackets.app.utils.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonUnquotedLiteral
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal

class BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.DOUBLE)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: BigDecimal) {
        val valueAsString = value.toPlainString()

        if (encoder is JsonEncoder) encoder.encodeJsonElement(JsonUnquotedLiteral(valueAsString))
        else encoder.encodeString(valueAsString)
    }

    override fun deserialize(decoder: Decoder): BigDecimal =
        if (decoder is JsonDecoder) BigDecimal(decoder.decodeJsonElement().jsonPrimitive.content)
        else BigDecimal(decoder.decodeString())
}