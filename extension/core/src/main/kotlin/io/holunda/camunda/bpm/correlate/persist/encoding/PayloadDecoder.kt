package io.holunda.camunda.bpm.correlate.persist.encoding

import io.holunda.camunda.bpm.correlate.correlation.metadata.TypeInfo

/**
 * Decoder to retrieve the typed payload from the encoded byte payload.
 */
interface PayloadDecoder {

  /**
   * Checks if the decoder supports the payload encoding.
   */
  fun supports(payloadEncoding: String): Boolean
  /**
   * Decodes the payload.
   * @param payload payload bytes.
   * @param payloadTypeInfo type information.
   * @return resulting object.
   */
  fun <T> decode(payloadTypeInfo: TypeInfo, payload: ByteArray): T
}
