package io.holunda.camunda.bpm.correlate.persist.encoding

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo

/**
 * Decoder of JSON payload encoded by Jackson.
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
}
