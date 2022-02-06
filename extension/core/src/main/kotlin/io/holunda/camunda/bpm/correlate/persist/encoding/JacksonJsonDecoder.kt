package io.holunda.camunda.bpm.correlate.persist.encoding

import com.fasterxml.jackson.databind.ObjectMapper
import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo

/**
 * Decoder of JSON payload.
 */
class JacksonJsonDecoder(
  private val objectMapper: ObjectMapper
) : PayloadDecoder {

  override fun <T> decode(payloadTypeInfo: TypeInfo, payload: ByteArray): T {
    val type = objectMapper.typeFactory.constructFromCanonical(
      "${payloadTypeInfo.namespace}.${payloadTypeInfo.name}"
    )
    return objectMapper.readValue(payload, type)
  }

}
