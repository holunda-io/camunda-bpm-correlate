package io.holunda.camunda.bpm.correlate.persist.encoding

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo

/**
 * Encoder/Decoder of JSON payload using Jackson.
 */
class JacksonJsonDecoder(
  private val objectMapper: ObjectMapper
) : PayloadDecoder {

  companion object {
    const val ENCODING = "jackson"
  }

  override fun supports(payloadEncoding: String): Boolean = ENCODING == payloadEncoding

  override fun <T> decode(payloadTypeInfo: TypeInfo, payload: ByteArray): T {
    val type = objectMapper.typeFactory.constructFromCanonical(
      "${payloadTypeInfo.namespace}.${payloadTypeInfo.name}"
    )
    return objectMapper.readValue(payload, type)
  }

  override fun <T> encode(payload: T): ByteArray {
    return objectMapper.writeValueAsBytes(payload)
  }
}
